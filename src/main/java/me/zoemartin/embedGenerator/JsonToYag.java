package me.zoemartin.embedGenerator;

import com.google.gson.Gson;
import org.apache.commons.cli.*;
import org.apache.commons.text.StringEscapeUtils;

import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JsonToYag {
    private static final String DEFAULT_NAME = "embed";

    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        Options options = options();
        HelpFormatter formatter = new HelpFormatter();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("YAGPDB Embed Generator", options);
            return;
        }

        Embed embed;

        String uri;
        if (cmd.hasOption("u")) {
            try {
                embed = new Gson().fromJson(jsonFromUrl(cmd.getOptionValue("u")), Embed.class);
            } catch (IOException | InterruptedException e) {
                System.err.println("There was an error requesting that URL");
                return;
            } catch (RuntimeException e) {
                System.err.println("There was an error while trying to parse the remote file.");
                return;
            }
        } else if (cmd.hasOption("f")) {
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(cmd.getOptionValue("f")));
            } catch (IOException e) {
                System.err.println("There was a error while trying to read that file.");
                return;
            } catch (RuntimeException e) {
                System.err.println("There was an error while trying to parse the local file.");
                return;
            }

            embed = new Gson().fromJson(br.lines().collect(Collectors.joining("\n")), Embed.class);
        } else {
            System.err.println("No Input specified!");
            formatter.printHelp("YAGPDB Embed Generator", options);
            return;
        }

        System.out.println(convert(embed, cmd.hasOption("n") ? cmd.getOptionValue("n") : DEFAULT_NAME));
    }

    private static Options options() {
        final Options options = new Options();

        Option name = Option.builder("n")
                          .longOpt("name").hasArg(true).required(false)
                          .desc("The Name of the YAGPDB var containing the custom embed")
                          .build();
        Option url = Option.builder("u").longOpt("url").hasArg(true)
                         .desc("The url pointing to a remote json file")
                         .build();
        Option file = Option.builder("f").longOpt("file").hasArg(true).required()
                          .desc("The path of a local json file")
                          .build();
        OptionGroup input = new OptionGroup();
        input.setRequired(true);
        input.addOption(url);
        input.addOption(file);

        options.addOptionGroup(input);
        options.addOption(name);

        return options;
    }

    public static String jsonFromUrl(String url) throws IOException, InterruptedException {
        Matcher paste = Pattern.compile(
            "(?:(?:http(?:s)?://)?(pastebin.com|hastebin.com|starb.in)/(?:raw/)?)([-a-zA-Z0-9@:%_+.~#?&=]*)"
        ).matcher(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;
        if (paste.find()) {
            request = HttpRequest.newBuilder()
                          .uri(URI.create("https://" + paste.group(1) + "/raw/" + paste.group(2)))
                          .build();
        } else {
            request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        }

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private static String convert(Embed e, String name) {
        Map<String, String> simple = new HashMap<>();
        List<Map<String, String>> fields = new LinkedList<>();
        Map<String, String> footer = new HashMap<>();
        Map<String, String> image = new HashMap<>();
        Map<String, String> thumbnail = new HashMap<>();
        Map<String, String> author = new HashMap<>();

        if (e.getTitle() != null) simple.put("title", e.getTitle());
        if (e.getDescription() != null) simple.put("description", escape(e.getDescription()));
        if (e.getUrl() != null) simple.put("url", e.getUrl());
        if (e.getTimestamp() != null) simple.put("timestamp", e.getTimestamp());
        if (e.getColor() != null) simple.put("color", String.valueOf(e.getColor()));
        if (e.getFooter() != null) {
            if (e.getFooter().getText() != null) footer.put("text", e.getFooter().getText());
            if (e.getFooter().getIcon_url() != null) footer.put("icon_url", e.getFooter().getText());
        }

        if (e.getImage() != null && e.getImage().getUrl() != null) image.put("url", e.getImage().getUrl());
        if (e.getThumbnail() != null && e.getThumbnail().getUrl() != null)
            thumbnail.put("url", e.getThumbnail().getUrl());

        if (e.getAuthor() != null) {
            if (e.getAuthor().getName() != null) author.put("name", e.getAuthor().getName());
            if (e.getAuthor().getUrl() != null) author.put("url", e.getAuthor().getUrl());
            if (e.getAuthor().getIcon_url() != null) author.put("icon_url", e.getAuthor().getIcon_url());
        }

        if (e.getFields() != null) {
            e.getFields().forEach(field -> {
                Map<String, String> f = new HashMap<>();
                if (field.getName() != null) f.put("name", field.getName());
                if (field.getValue() != null) f.put("value", escape(field.getValue()));
                if (field.getInline() != null) f.put("inline", String.valueOf(field.getInline()));
                if (!f.isEmpty()) fields.add(f);
            });
        }

        StringBuilder output = new StringBuilder();

        output.append("{{ $").append(name).append(" := cembed\n");

        if (!simple.isEmpty())
            output.append(simple.entrySet().stream()
                              .map(entry -> String.format(entry.getValue().matches(
                                  "\\d+|true|false") ? "\t\"%s\" %s" : "\t\"%s\" \"%s\"",
                                  entry.getKey(), entry.getValue())).collect(Collectors.joining("\n")))
                .append("\n");


        if (!fields.isEmpty()) {
            output.append("\t").append("\"fields\" (cslice\n");
            output.append(fields.stream().map(m -> String.format("\t\t%s",
                convertMap(m))).collect(Collectors.joining("\n")));
            output.append("\t").append(")\n");
        }

        if (!footer.isEmpty()) output.append("\t").append("\"footer\"").append(convertMap(footer)).append("\n");
        if (!image.isEmpty()) output.append("\t").append("\"image\"").append(convertMap(image)).append("\n");
        if (!thumbnail.isEmpty())
            output.append("\t").append("\"thumbnail\"").append(convertMap(thumbnail)).append("\n");
        if (!author.isEmpty()) output.append("\t").append("\"author\"").append(convertMap(author)).append("\n");

        output.append("}}");

        return output.toString();

    }

    private static String convertMap(Map<String, String> map) {
        return String.format(" (sdict %s)",
            map.entrySet().stream().map(entry -> String.format(entry.getValue().matches(
                "\\d+|true|false") ? "\"%s\" %s" : "\"%s\" \"%s\"",
                entry.getKey(), entry.getValue())).collect(Collectors.joining(" ")));
    }

    private static String escape(String string) {
        return StringEscapeUtils.escapeJava(string);
    }
}

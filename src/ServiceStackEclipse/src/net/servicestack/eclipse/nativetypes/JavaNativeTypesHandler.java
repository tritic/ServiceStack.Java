package net.servicestack.eclipse.nativetypes;

import org.apache.http.client.utils.URIBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class JavaNativeTypesHandler implements INativeTypesHandler {

    public Map<String, String> parseComments(String codeOutput) {
        Map<String, String> result = new HashMap<>();
        Scanner scanner = new Scanner(codeOutput);
        List<String> linesOfCode = new ArrayList<>();
        Integer i = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            linesOfCode.add(line);
            String configLine = linesOfCode.get(i);
            if (!configLine.startsWith("//") && configLine.contains(":")) {
                String[] keyVal = configLine.split(":");
                result.put(keyVal[0].substring(2).trim(), keyVal[1].trim());
            }
            if (line.startsWith("*/")) break;
            i++;
        }
        scanner.close();
        return result;
    }


    public String generateUrl(String baseUrl, Map<String, String> options) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        
        URIBuilder builder;
        try {
            builder = new URIBuilder(baseUrl);
        } catch (URISyntaxException e) {
            //Log error to IDEA warning bubble/window.
            return null;
        }

        String existingPath = builder.getPath();
        if (existingPath == null || existingPath.equals("/")) {
            builder.setPath("/types/java");
        } else {
            builder.setPath(existingPath + "/types/java");
        }
        if(options != null) {
            for (Map.Entry<String, String> item : options.entrySet()) {
                builder.addParameter(item.getKey(), item.getValue().trim());
            }
        }
        try {
            return builder.build().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getUpdatedCode(String baseUrl, Map<String, String> options) throws IOException {
        String result = null;
        String url = generateUrl(baseUrl, options);
        URL javaCodeUrl = new URL(url);

        URLConnection javaCodeConnection = javaCodeUrl.openConnection();
        javaCodeConnection.setRequestProperty("content-type", "application/json; charset=utf-8");
        BufferedReader javaCodeBufferReader = new BufferedReader(
                new InputStreamReader(
                        javaCodeConnection.getInputStream()));
        String javaCodeInput;
        StringBuilder javaCodeResponse = new StringBuilder();
        while ((javaCodeInput = javaCodeBufferReader.readLine()) != null) {
            javaCodeResponse.append(javaCodeInput);
            //All documents inside IntelliJ IDEA always use \n line separators.
            //http://confluence.jetbrains.net/display/IDEADEV/IntelliJ+IDEA+Architectural+Overview
            javaCodeResponse.append("\n");
        }

        String javaCode = javaCodeResponse.toString();
        if (!javaCode.startsWith("/* Options:")) {
            return null;
        }
        result = javaCode;
        return result;
    }


    public NativeTypesLanguage getTypesLanguage() {
        return NativeTypesLanguage.Java;
    }

    @Override
    public boolean validateServiceStackEndpoint(String baseUrl) throws IOException {
        String javaCode = getUpdatedCode(baseUrl, null);
        return javaCode != null && isFileAServiceStackReference(javaCode);
    }

    public boolean isFileAServiceStackReference(String fileContents) {
        return fileContents.startsWith("/* Options:");
    }


    public String getRelativeTypesUrl() {
        return "types/java";
    }
}
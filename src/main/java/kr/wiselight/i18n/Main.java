package kr.wiselight.i18n;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    public static class Node {
        private final Map<String, Node> children = new HashMap<>();

        public Node getOrCreateChild(String key) {
            return children.computeIfAbsent(key, k -> new Node());
        }
    }

    public static void generateClass(FileWriter writer, Node node, String parentKey, String indentation) throws IOException {
        for (Map.Entry<String, Node> entry : node.children.entrySet()) {
            String key = entry.getKey();
            Node child = entry.getValue();

            String newKey = (parentKey.isEmpty() ? "" : (parentKey + ".")) + key;

            if (child.children.isEmpty()) {
                writer.write(indentation + "public static final String " + key + " = \"" + newKey + "\";\n");
            } else {
                writer.write(indentation + "public static final class " + key + " {\n");
                generateClass(writer, child, newKey, indentation + "    ");
                writer.write(indentation + "}\n");
            }
        }
    }

    public static void main(String[] args) {
        Properties prop = new Properties();
        FileInputStream input = null;

        String packageStr = args[0];
        String srcPropertieFilePath = args[1];
        String destClassPath = args[2];

        try {
            input = new FileInputStream(srcPropertieFilePath);

            // Properties 파일을 로드합니다.
            prop.load(input);

            Node root = new Node();

            for (String key : prop.stringPropertyNames()) {
                String[] parts = key.split("\\.");
                Node current = root;

                for (String part : parts) {
                    current = current.getOrCreateChild(part);
                }
            }

            FileWriter writer = new FileWriter(destClassPath + "/I18N.java");
            writer.write("package ");
            writer.write(packageStr);
            writer.write(";\n\n");
            writer.write("public final class I18N {\n");

            generateClass(writer, root, "", "    ");

            writer.write("}\n");
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
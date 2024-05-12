package raphael.onlinehousereservation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class SaveExceptions {
    private static final String FILE_NAME = "Exceptions.xml";

    public static void saveExceptionToXml(Exception exception) {
        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            String exceptionXml = "<exception>"
                    + "<message>" + escapeXml(exception.getMessage()) + "</message>"
                    + "<stackTrace>" + escapeXml(getStackTraceAsString(exception)) + "</stackTrace>"
                    + "</exception>";

            printWriter.println(exceptionXml);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String escapeXml(String input) {
        return input.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&apos;");
    }

    private static String getStackTraceAsString(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        printWriter.flush();
        return stringWriter.toString();
    }
}

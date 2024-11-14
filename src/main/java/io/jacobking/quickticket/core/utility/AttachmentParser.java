package io.jacobking.quickticket.core.utility;

public class AttachmentParser {

    private static final String[] DOCUMENT_TYPES = {
            ".pdf", ".docx", ".xlsx", ".one", ".txt", ".doc", ".odt", ".rtf", ".csv", ".xls",
            ".ppt", ".pptx", ".jpg", ".jpeg", ".png", ".gif", ".dwg", ".html", ".htm",
            ".zip", ".rar", "7z"
    };

    private static final String[] LINK_TYPES = {
            "https", "http"
    };

    private final String attachmentPath;

    public AttachmentParser(final String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public AttachmentType findType() {
        for (final String documentType : DOCUMENT_TYPES) {
            if (attachmentPath.endsWith(documentType))
                return AttachmentType.DOCUMENT;
        }

        for (final String linkType : LINK_TYPES) {
            if (attachmentPath.startsWith(linkType))
                return AttachmentType.LINK;
        }

        return AttachmentType.UNKNOWN;
    }


    public enum AttachmentType {
        DOCUMENT, LINK, UNKNOWN
    }

}

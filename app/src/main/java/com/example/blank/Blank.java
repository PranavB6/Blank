package com.example.blank;

import com.google.firebase.firestore.DocumentId;

public class Blank {
    @DocumentId
    String documentId;

    String attribute;

    Blank() { }

    Blank(String attribute) {
        this.attribute = attribute;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}

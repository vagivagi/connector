package com.vagivagi.connector.common;

public class ConnectorResponseBody {
    private String message;
    private ThirdResponseBody body;

    public ConnectorResponseBody(String message, ThirdResponseBody body) {
        this.message = message;
        this.body = body;
    }

    public static ConnectorResponseBodyBuilder builder() {
        return new ConnectorResponseBodyBuilder();
    }

    public String getMessage() {
        return message;
    }

    public ThirdResponseBody getBody() {
        return body;
    }

    public static class ConnectorResponseBodyBuilder {
        private String message;
        private ThirdResponseBody body;

        public ConnectorResponseBody build() {
            return new ConnectorResponseBody(message, body);
        }

        public ConnectorResponseBodyBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ConnectorResponseBodyBuilder body(ThirdResponseBody body) {
            this.body = body;
            return this;
        }
    }
}

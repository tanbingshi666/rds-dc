package com.skysec.soc.rds.dc.exception;

public class ElasticsearchConnectorException extends RuntimeException {

    public ElasticsearchConnectorException(String message) {
        super(message);
    }

    public ElasticsearchConnectorException(String message, Throwable cause) {
        super(message, cause);
    }
}

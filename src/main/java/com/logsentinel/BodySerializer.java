package com.logsentinel;

/**
 * Interface that should be implemented if you want to encode the request body with a particular format (protobuf, thrift, etc).
 * Note that you should Base64 encode the binary output.
 * 
 * By default JSON is used.
 * 
 * @author bozho
 *
 */
public interface BodySerializer {

    String serialize(Object object);
}

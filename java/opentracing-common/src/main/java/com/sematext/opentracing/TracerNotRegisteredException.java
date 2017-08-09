/*
 *    Copyright (c) Sematext International
 *    All Rights Reserved
 *
 *    THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Sematext International
 *    The copyright notice above does not evidence any
 *    actual or intended publication of such source code.
 */
package com.sematext.opentracing;

public class TracerNotRegisteredException extends RuntimeException {

    public TracerNotRegisteredException(String message) {
        super(message);
    }
}

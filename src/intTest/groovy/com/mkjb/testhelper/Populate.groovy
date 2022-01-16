package com.mkjb.testhelper


import org.spockframework.runtime.extension.ExtensionAnnotation

import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.METHOD
import static java.lang.annotation.ElementType.TYPE
import static java.lang.annotation.RetentionPolicy.RUNTIME

@Target([TYPE, METHOD])
@Retention(RUNTIME)
@ExtensionAnnotation(PopulateExtension)
@interface Populate {
    String[] from() default ['notes.json']
    String db() default 'notes'
    String[] coll() default ['notes']
}
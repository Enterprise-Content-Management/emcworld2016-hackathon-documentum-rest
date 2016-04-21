/*
 * Copyright (c) 2016. EMC Coporation. All Rights Reserved.
 */

package com.emc.documentum.constants;

public class LinkRelation {
    public static final String SELF = "self";
    public static final String EDIT = "edit";
    public static final String CONTENTS = "contents";

    public static final String DCTM_LINKREL_PREFIX = "http://identifiers.emc.com/linkrel/";

    public static final String REPOSITORIES = DCTM_LINKREL_PREFIX + "repositories";
    public static final String DQL = DCTM_LINKREL_PREFIX + "dql";
    public static final String CABINETS = DCTM_LINKREL_PREFIX + "cabinets";
    public static final String OBJECTS = DCTM_LINKREL_PREFIX + "objects";
    public static final String FOLDERS = DCTM_LINKREL_PREFIX + "folders";
    public static final String PARENT_LINKS = DCTM_LINKREL_PREFIX + DCTM_LINKREL_PREFIX + "parent-links";
    public static final String PRIMARY_CONTENT = DCTM_LINKREL_PREFIX + "primary-content";
    public static final String CONTENT_MEDIA = DCTM_LINKREL_PREFIX + "content-media";
    public static final String DOCUMENTS = DCTM_LINKREL_PREFIX + "documents";
    public static final String SEARCH = DCTM_LINKREL_PREFIX + "search";
}

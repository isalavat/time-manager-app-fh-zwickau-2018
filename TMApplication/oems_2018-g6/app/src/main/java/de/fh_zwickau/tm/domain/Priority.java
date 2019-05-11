package de.fh_zwickau.tm.domain;

import java.io.Serializable;

public enum Priority implements Serializable{
    URGENT_AND_IMPORTANT,
    NOT_URGENT_AND_IMPORTANT,
    URGENT_AND_NOT_IMPORTANT,
    NOT_URGENT_AND_NOT_IMPORTANT
}

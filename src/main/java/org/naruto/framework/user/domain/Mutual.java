package org.naruto.framework.user.domain;

public enum Mutual{

    BOTH("both"),FOLLOW("follow")
    ;

    private String value;
    private Mutual(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
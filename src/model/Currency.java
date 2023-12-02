package model;
/*
@date 28.11.2023
@author Sergey Bugaienko
*/

import java.util.Objects;

public class Currency {
    private final String code;
    private String title;

    public Currency(String code, String title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public String toString() {
        return "{" +
                code +
                ": " + title +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Currency currency = (Currency) o;

        return Objects.equals(code, currency.code);
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

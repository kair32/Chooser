package com.AKS.chooser;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

class Variant {
    String hint;
    String text;
    Uri UriImage;
    boolean variant;

    Variant(String hint, boolean variant) {
        this.hint = hint;
        this.variant = variant;
    }
    Variant(String hint, String text, boolean variant ) {
        this.hint = hint;
        this.text = text;
        this.variant = variant;
    }
}
class VariantHistory {
    String HeadLine;
    List<Variant> variant;
    Boolean varChoice;
    VariantHistory(String headline, List<Variant>  list){
        this.HeadLine = headline;
        variant = new ArrayList<>();
        variant.addAll(0, list);
    }

}
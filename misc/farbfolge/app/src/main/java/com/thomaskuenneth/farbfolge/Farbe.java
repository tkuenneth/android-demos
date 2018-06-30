package com.thomaskuenneth.farbfolge;

enum Farbe {

    ROT(0xff800000, 0xffff0000),
    GELB(0xff808000, 0xffffff00),
    GRUEN(0xff008000, 0xff00ff00),
    BLAU(0xff000080, 0xff0000ff);

    private int farbeDunkel;
    private int farbeHell;

    Farbe(int dunkel, int hell) {
        farbeDunkel = dunkel;
        farbeHell = hell;
    }

    public int getFarbeDunkel() {
        return farbeDunkel;
    }

    public int getFarbeHell() {
        return farbeHell;
    }
}

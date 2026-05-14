package model.enums;

public enum SchoolCode {

    OIL_AND_GAS       ("01", "Faculty of Energy and Oil & Gas Industry"),
    GEOLOGY           ("02", "Faculty of Geology and Geological Exploration"),
    FIT               ("03", "Faculty of Information Technology (FIT/SITE)"),
    MATHEMATICS       ("04", "School of Mathematics and Cybernetics"),
    CHEMICAL          ("05", "School of Chemical Engineering"),
    BUSINESS          ("06", "Business School (KBS)"),
    ECONOMICS         ("07", "International School of Economics (ISE)"),
    MARITIME          ("08", "Kazakhstan Maritime Academy (KMA)"),
    GENERAL           ("09", "Faculty of General Education");

    private final String code;
    private final String displayName;

    SchoolCode(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return code + " - " + displayName; }
}
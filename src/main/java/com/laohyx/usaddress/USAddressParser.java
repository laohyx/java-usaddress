package com.laohyx.usaddress;

import com.github.jcrfsuite.CrfTagger;
import com.github.jcrfsuite.util.Pair;
import com.laohyx.usaddress.feature.BoolFeature;
import com.laohyx.usaddress.feature.DictFeature;
import com.laohyx.usaddress.feature.Feature;
import com.laohyx.usaddress.feature.StringFeature;
import org.apache.commons.lang3.StringUtils;
import third_party.org.chokkan.crfsuite.ItemSequence;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class USAddressParser {

    public static final String[] LABLES = new String[]{
            "AddressNumberPrefix",
            "AddressNumber",
            "AddressNumberSuffix",
            "StreetNamePreModifier",
            "StreetNamePreDirectional",
            "StreetNamePreType",
            "StreetName",
            "StreetNamePostType",
            "StreetNamePostDirectional",
            "SubaddressType",
            "SubaddressIdentifier",
            "BuildingName",
            "OccupancyType",
            "OccupancyIdentifier",
            "CornerOf",
            "LandmarkName",
            "PlaceName",
            "StateName",
            "ZipCode",
            "USPSBoxType",
            "USPSBoxID",
            "USPSBoxGroupType",
            "USPSBoxGroupID",
            "IntersectionSeparator",
            "Recipient",
            "NotAddress"
    };

    public static final String PARENT_LABEL = "AddressString";
    public static final String GROUP_LABEL = "AddressCollection";

    public static final String MODEL_FILE = "usaddr.crfsuite";
    public static final String MODEL_PATH = "./" + MODEL_FILE;

    public static final Set<String> DIRECTIONS = new HashSet<>(Arrays.asList("n", "s", "e", "w",
            "ne", "nw", "se", "sw",
            "north", "south", "east", "west",
            "northeast", "northwest", "southeast", "southwest"));

    public static final Set<String> STREET_NAMES = new HashSet<>(Arrays.asList(
        "allee", "alley", "ally", "aly", "anex", "annex", "annx", "anx",
                "arc", "arcade", "av", "ave", "aven", "avenu", "avenue", "avn", "avnue",
                "bayoo", "bayou", "bch", "beach", "bend", "bg", "bgs", "bl", "blf",
                "blfs", "bluf", "bluff", "bluffs", "blvd", "bnd", "bot", "bottm",
                "bottom", "boul", "boulevard", "boulv", "br", "branch", "brdge", "brg",
                "bridge", "brk", "brks", "brnch", "brook", "brooks", "btm", "burg",
                "burgs", "byp", "bypa", "bypas", "bypass", "byps", "byu", "camp", "canyn",
                "canyon", "cape", "causeway", "causwa", "causway", "cen", "cent",
                "center", "centers", "centr", "centre", "ci", "cir", "circ", "circl",
                "circle", "circles", "cirs", "ck", "clb", "clf", "clfs", "cliff",
                "cliffs", "club", "cmn", "cmns", "cmp", "cnter", "cntr", "cnyn", "common",
                "commons", "cor", "corner", "corners", "cors", "course", "court",
                "courts", "cove", "coves", "cp", "cpe", "cr", "crcl", "crcle", "crecent",
                "creek", "cres", "crescent", "cresent", "crest", "crk", "crossing",
                "crossroad", "crossroads", "crscnt", "crse", "crsent", "crsnt", "crssing",
                "crssng", "crst", "crt", "cswy", "ct", "ctr", "ctrs", "cts", "curv",
                "curve", "cv", "cvs", "cyn", "dale", "dam", "div", "divide", "dl", "dm",
                "dr", "driv", "drive", "drives", "drs", "drv", "dv", "dvd", "est",
                "estate", "estates", "ests", "ex", "exp", "expr", "express", "expressway",
                "expw", "expy", "ext", "extension", "extensions", "extn", "extnsn",
                "exts", "fall", "falls", "ferry", "field", "fields", "flat", "flats",
                "fld", "flds", "fls", "flt", "flts", "ford", "fords", "forest", "forests",
                "forg", "forge", "forges", "fork", "forks", "fort", "frd", "frds",
                "freeway", "freewy", "frg", "frgs", "frk", "frks", "frry", "frst", "frt",
                "frway", "frwy", "fry", "ft", "fwy", "garden", "gardens", "gardn",
                "gateway", "gatewy", "gatway", "gdn", "gdns", "glen", "glens", "gln",
                "glns", "grden", "grdn", "grdns", "green", "greens", "grn", "grns",
                "grov", "grove", "groves", "grv", "grvs", "gtway", "gtwy", "harb",
                "harbor", "harbors", "harbr", "haven", "havn", "hbr", "hbrs", "height",
                "heights", "hgts", "highway", "highwy", "hill", "hills", "hiway", "hiwy",
                "hl", "hllw", "hls", "hollow", "hollows", "holw", "holws", "hrbor", "ht",
                "hts", "hvn", "hway", "hwy", "inlet", "inlt", "is", "island", "islands",
                "isle", "isles", "islnd", "islnds", "iss", "jct", "jction", "jctn",
                "jctns", "jcts", "junction", "junctions", "junctn", "juncton", "key",
                "keys", "knl", "knls", "knol", "knoll", "knolls", "ky", "kys", "la",
                "lake", "lakes", "land", "landing", "lane", "lanes", "lck", "lcks", "ldg",
                "ldge", "lf", "lgt", "lgts", "light", "lights", "lk", "lks", "ln", "lndg",
                "lndng", "loaf", "lock", "locks", "lodg", "lodge", "loop", "loops", "lp",
                "mall", "manor", "manors", "mdw", "mdws", "meadow", "meadows", "medows",
                "mews", "mi", "mile", "mill", "mills", "mission", "missn", "ml", "mls",
                "mn", "mnr", "mnrs", "mnt", "mntain", "mntn", "mntns", "motorway",
                "mount", "mountain", "mountains", "mountin", "msn", "mssn", "mt", "mtin",
                "mtn", "mtns", "mtwy", "nck", "neck", "opas", "orch", "orchard", "orchrd",
                "oval", "overlook", "overpass", "ovl", "ovlk", "park", "parks", "parkway",
                "parkways", "parkwy", "pass", "passage", "path", "paths", "pike", "pikes",
                "pine", "pines", "pk", "pkway", "pkwy", "pkwys", "pky", "pl", "place",
                "plain", "plaines", "plains", "plaza", "pln", "plns", "plz", "plza",
                "pne", "pnes", "point", "points", "port", "ports", "pr", "prairie",
                "prarie", "prk", "prr", "prt", "prts", "psge", "pt", "pts", "pw", "pwy",
                "rad", "radial", "radiel", "radl", "ramp", "ranch", "ranches", "rapid",
                "rapids", "rd", "rdg", "rdge", "rdgs", "rds", "rest", "ri", "ridge",
                "ridges", "rise", "riv", "river", "rivr", "rn", "rnch", "rnchs", "road",
                "roads", "route", "row", "rpd", "rpds", "rst", "rte", "rue", "run", "rvr",
                "shl", "shls", "shoal", "shoals", "shoar", "shoars", "shore", "shores",
                "shr", "shrs", "skwy", "skyway", "smt", "spg", "spgs", "spng", "spngs",
                "spring", "springs", "sprng", "sprngs", "spur", "spurs", "sq", "sqr",
                "sqre", "sqrs", "sqs", "squ", "square", "squares", "st", "sta", "station",
                "statn", "stn", "str", "stra", "strav", "strave", "straven", "stravenue",
                "stravn", "stream", "street", "streets", "streme", "strm", "strt",
                "strvn", "strvnue", "sts", "sumit", "sumitt", "summit", "te", "ter",
                "terr", "terrace", "throughway", "tl", "tpk", "tpke", "tr", "trace",
                "traces", "track", "tracks", "trafficway", "trail", "trailer", "trails",
                "trak", "trce", "trfy", "trk", "trks", "trl", "trlr", "trlrs", "trls",
                "trnpk", "trpk", "trwy", "tunel", "tunl", "tunls", "tunnel", "tunnels",
                "tunnl", "turn", "turnpike", "turnpk", "un", "underpass", "union",
                "unions", "uns", "upas", "valley", "valleys", "vally", "vdct", "via",
                "viadct", "viaduct", "view", "views", "vill", "villag", "village",
                "villages", "ville", "villg", "villiage", "vis", "vist", "vista", "vl",
                "vlg", "vlgs", "vlly", "vly", "vlys", "vst", "vsta", "vw", "vws", "walk",
                "walks", "wall", "way", "ways", "well", "wells", "wl", "wls", "wy", "xc",
                "xg", "xing", "xrd", "xrds"));

    public static CrfTagger tagger;

    static {
        tagger = new CrfTagger(MODEL_PATH);
    }


    public static List<Pair<String, String>> parse(String addressString) {
        List<Pair<String, String>> result = new ArrayList<>();
        List<String> tokens = tokenize(addressString);
        if (tokens.size() == 0)
            return result;
        List<DictFeature> features = tokens2features(tokens);

        // Debug json string
//        System.out.println(features.toString());

        ItemSequence seq = new ItemSequence();
        for (DictFeature f : features) {
            seq.add(f.toItem());
        }

        List<Pair<String, Double>> tags = tagger.tag(seq);
        for (int i = 0; i < tags.size(); i++) {
            result.add(new Pair<>(tokens.get(i), tags.get(i).getFirst()));
        }
        return result;
    }

    public static List<String> tokenize(String addressString) {
        List<String> result = new ArrayList<>();
        addressString = addressString.replaceAll("(&#38;)|(&amp;)", "&");
        String pattern = "\\(*\\b[^\\s,;#&()]+[.,;)\\n]*|[#&]";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(addressString);
        while (m.find()) {
            result.add(m.group(0));
        }
        return result;
    }

    public static List<DictFeature> tokens2features(List<String> tokens) {
        List<DictFeature> featureSequence = new ArrayList<>();
        featureSequence.add(tokenFeatures(tokens.get(0)));
        DictFeature previousFeatures = featureSequence.get(featureSequence.size() - 1).copy();

        for (int i = 1; i < tokens.size(); i++) {
            String token = tokens.get(i);
            DictFeature tokenFeatures = tokenFeatures(token);
            DictFeature currentFeatures = tokenFeatures.copy();

            featureSequence.get(featureSequence.size() - 1).put("next", currentFeatures);
            tokenFeatures.put("previous", previousFeatures);

            featureSequence.add(tokenFeatures);

            previousFeatures = currentFeatures;
        }
        featureSequence.get(0).put("address.start", true);
        featureSequence.get(featureSequence.size() - 1).put("address.end", true);

        if (featureSequence.size() > 1) {
            featureSequence.get(1).getDictByKey("previous").put("address.start", true);
            featureSequence.get(featureSequence.size() - 2).getDictByKey("next").put("address.end", true);
        }
        return featureSequence;
    }

    public static DictFeature tokenFeatures(String token) {
        String tokenClean;
        if(token.equals("&") || token.equals("#") || token.equals("½")) {
            tokenClean = token;
        } else  {
            tokenClean = token.replaceAll("(^[\\W]*)|([^.\\w]*$)","");
        }

        String tokenAbbrev = tokenClean.toLowerCase().replaceAll("[.]", "");
        DictFeature f = new DictFeature();
        f.put("abbrev", tokenClean.charAt(tokenClean.length() - 1) == '.');
        f.put("digits", digits(tokenClean));
        f.put("word", !StringUtils.isNumeric(tokenAbbrev) ? new StringFeature(tokenAbbrev) : new BoolFeature(false));
        f.put("trailing.zeros", StringUtils.isNumeric(tokenAbbrev) ? new StringFeature(trailingZeros(tokenAbbrev)) : new BoolFeature(false));
        f.put("length", StringUtils.isNumeric(tokenAbbrev) ? "d:" + String.valueOf(tokenAbbrev.length()) :
                "w:" + String.valueOf(tokenAbbrev.length()));
        f.put("endsinpunc", Feature.createFromEndsinpunc(token));
        f.put("directional", DIRECTIONS.contains(tokenAbbrev));
        f.put("street_name", STREET_NAMES.contains(tokenAbbrev));
        f.put("has.vowels", stringIntersect(tokenAbbrev.substring(1), "aeiou").size() > 0);
        return f;
    }

//    def trailingZeros(token):
//    results = re.findall(r'(0+)$', token)
//            if results:
//            return results[0]
//            else:
//            return ''
    public static String trailingZeros(String token) {
        String pattern = "(0+)$";

        Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(token);
        if (m.find()) {
            return m.group(1);
        } else {
            return "";
        }
    }

//    def digits(token):
//            if token.isdigit():
//            return 'all_digits'
//    elif set(token) & set(string.digits):
//            return 'some_digits'
//            else:
//            return 'no_digits'

    public static String digits(String token) {
        if(StringUtils.isNumeric(token)) {
            return "all_digits";
        } else {
            if (stringIntersect(token, "0123456789").size() > 0) {
                return "some_digits";
            } else {
                return "no_digits";
            }
        }
    }

    public static Set<Character> stringIntersect(String str1, String str2) {
        Set<Character> set1 = new HashSet<>();
        for (char c : str1.toCharArray()) {
            set1.add(c);
        }
        Set<Character> set2 = new HashSet<>();
        for (char c : str2.toCharArray()) {
            set2.add(c);
        }
        set1.retainAll(set2);
        return set1;

    }
}

package com.laohyx.usaddress;

import com.github.jcrfsuite.util.Pair;

import java.util.List;

public class Demo {
    public static void main(String[] argv) {
        {
            String addr = "123 Main St. Suite 100 Chicago, IL";
            List<Pair<String, String>> result = USAddressParser.parse(addr);
            for (Pair<String, String> pair : result) {
                System.out.println(pair.getFirst() + ": " + pair.getSecond());
            }
            /*
            Expected output:
                123: AddressNumber
                Main: StreetName
                St.: StreetNamePostType
                Suite: OccupancyType
                100: OccupancyIdentifier
                Chicago,: PlaceName
                IL: StateName
             */
        }

        System.out.println("--------------------------------------------");

        {
            String addr = "1775 Broadway And 57th, Newyork NY";
            List<Pair<String, String>> result = USAddressParser.parse(addr);
            for (Pair<String, String> pair : result) {
                System.out.println(pair.getFirst() + ": " + pair.getSecond());
            }
            /*
            Expected output:
                1775: AddressNumber
                Broadway: StreetName
                And: IntersectionSeparator
                57th,: StreetName
                Newyork: PlaceName
                NY: StateName
             */
        }

    }
}

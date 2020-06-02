package com.laohyx.usaddress;

import static org.junit.jupiter.api.Assertions.*;

import com.github.jcrfsuite.util.Pair;
import java.util.List;
import org.junit.jupiter.api.Test;

class USAddressParserTest {

  @Test
  void parse1() {
    String addr = "123 Main St. Suite 100 Chicago, IL";
    List<Pair<String, String>> result = USAddressParser.parse(addr);
    assertEquals(7, result.size());
    assertPairEquals(new Pair<>("123", "AddressNumber"), result.get(0));
    assertPairEquals(new Pair<>("Main", "StreetName"), result.get(1));
    assertPairEquals(new Pair<>("St.", "StreetNamePostType"), result.get(2));
    assertPairEquals(new Pair<>("Suite", "OccupancyType"), result.get(3));
    assertPairEquals(new Pair<>("100","OccupancyIdentifier"), result.get(4));
    assertPairEquals(new Pair<>("Chicago,", "PlaceName"), result.get(5));
    assertPairEquals(new Pair<>("IL", "StateName"), result.get(6));
  }

  @Test
  void parse2() {
    String addr = "1775 Broadway And 57th, Newyork NY";
    List<Pair<String, String>> result = USAddressParser.parse(addr);
    assertEquals(6, result.size());
    assertPairEquals(new Pair<>("1775", "AddressNumber"), result.get(0));
    assertPairEquals(new Pair<>("Broadway", "StreetName"), result.get(1));
    assertPairEquals(new Pair<>("And", "IntersectionSeparator"), result.get(2));
    assertPairEquals(new Pair<>("57th,", "StreetName"), result.get(3));
    assertPairEquals(new Pair<>("Newyork", "PlaceName"), result.get(4));
    assertPairEquals(new Pair<>("NY", "StateName"), result.get(5));
  }

  private void assertPairEquals(Pair<String, String> p1, Pair<String, String> p2) {
    assertEquals(p1.first, p2.first);
    assertEquals(p1.second, p2.second);
  }
}
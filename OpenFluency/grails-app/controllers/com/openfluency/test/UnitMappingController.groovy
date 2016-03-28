package com.openfluency.test

class UnitMappingController {

    def index() {
		TestObj2 testobj = new TestObj2();
		List<String> testList = new ArrayList<String>();
		testList.add(testobj.f1);
		testList.add(testobj.f2);
		testList.add(testobj.f3);
		testList.add(testobj.f4);
		testList.add(testobj.f5);
		testList.add(testobj.f6);
		testList.add(testobj.f7);
		testList.add(testobj.f8);
		testList.add(testobj.f9);
		
		[test:testList]
	}
	
	def index2() {
		TestObj2 testobj = new TestObj2();
		List<String> testList = new ArrayList<String>();
		testList.add(testobj.f1);
		testList.add(testobj.f2);
		testList.add(testobj.f3);
		testList.add(testobj.f4);
		testList.add(testobj.f5);
		testList.add(testobj.f6);
		testList.add(testobj.f7);
		testList.add(testobj.f8);
		testList.add(testobj.f9);
		testList.add(testobj.f10);
		
		[test:testList]
	}
	
	def test() {
		TestObj2 testobj = new TestObj2();
		List<String> testList = new ArrayList<String>();
		testList.add(testobj.f1);
		testList.add(testobj.f2);
		testList.add(testobj.f3);
		testList.add(testobj.f4);
		testList.add(testobj.f5);
		testList.add(testobj.f6);
		testList.add(testobj.f7);
		testList.add(testobj.f8);
		testList.add(testobj.f9);
		testList.add(testobj.f10);
		
		[test:testList]
	}
}

class TestObj2 {
	public String f1 = "水";
	public String f2 = "85";
	public String f3 = "4";
	public String f4 = "氺";
	public String f5 = "水 永 泳 決 治 海 演 漢 瀬";
	public String f6 = "water";
	public String f7 = "みず";
	public String f8 = "ミズー";
	public String f9 = "mizɯ";
	public String f10 = "http://www.bestmotherofthegroomspeeches.com/wp-content/themes/thesis/rotator/sample-1.jpg"
}

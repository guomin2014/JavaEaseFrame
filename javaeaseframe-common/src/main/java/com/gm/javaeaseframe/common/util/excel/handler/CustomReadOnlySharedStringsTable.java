package com.gm.javaeaseframe.common.util.excel.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CustomReadOnlySharedStringsTable extends ReadOnlySharedStringsTable
{
	/**
     * An integer representing the total count of strings in the workbook. This count does not
     * include any numbers, it counts only the total of text strings in the workbook.
     */
    private int count;

    /**
     * An integer representing the total count of unique strings in the Shared String Table.
     * A string is unique even if it is a copy of another string, but has different formatting applied
     * at the character level.
     */
    private int uniqueCount;

    /**
     * The shared strings table.
     */
    private List<String> strings;
	private StringBuffer characters;
	private boolean tIsOpen;
	private boolean tIsSkip;
	public CustomReadOnlySharedStringsTable(OPCPackage pkg) throws IOException, SAXException
	{
		super(pkg);
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
	{
		if ("sst".equals(name)) {
            String count = attributes.getValue("count");
            if(count != null) this.count = Integer.parseInt(count);
            String uniqueCount = attributes.getValue("uniqueCount");
            if(uniqueCount != null) this.uniqueCount = Integer.parseInt(uniqueCount);
            this.strings = new ArrayList<String>(this.uniqueCount);
            characters = new StringBuffer();
        } else if ("si".equals(name)) {
            characters.setLength(0);
            tIsSkip = false;
        } else if ("t".equals(name)) {
            tIsOpen = true;
        } else if ("rPh".equals(name)) {
        	tIsSkip = true;
        }
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException
	{
		if ("si".equals(name)) {
            strings.add(characters.toString());
        } else if ("t".equals(name)) {
           tIsOpen = false;
        }
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if (tIsOpen && !tIsSkip)
            characters.append(ch, start, length);
	}

	@Override
	public int getCount()
	{
		return this.count;
	}

	@Override
	public int getUniqueCount()
	{
		return this.uniqueCount;
	}

	@Override
	public String getEntryAt(int idx)
	{
		return strings.get(idx);
	}

	@Override
	public List<String> getItems()
	{
		return strings;
	}
}

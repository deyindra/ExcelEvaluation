package org.idey.excel;

import java.util.HashMap;
import java.util.Map;

public enum  PositiveBaseConverterEnum {
    EXCEL_ENCODING(65,26) {
        @Override
        protected String generate(int val) {
            if (val < 0 || val > 127) {
                throw new IllegalArgumentException("Invalid ascii value");
            }
            return Character.toString((char) (val));
        }

        @Override
        String encode(int value) {
            if (value <= 0) {
                throw new IllegalArgumentException("Invalid Value");
            }
            String converted = "";
            String array[] = getEncodingArray();
            do {
                int remainder = value % array.length;
                if (remainder == 0) {
                    remainder = 26;
                }
                converted = array[remainder - 1] + converted;
                value = (value - remainder) / 26;
            } while (value > 0);
            return converted;
        }
    },
    BINARY(new String[]{"0", "1"}),
    HEX(new String[] {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"});



    private String[] encodingArray;
    private Map<String, Integer> mapIndexes;

    PositiveBaseConverterEnum(String[] encodingArray) {
        this.encodingArray = encodingArray;
        mapIndexes= new HashMap<>();
        int count=0;
        for(String val:encodingArray){
            mapIndexes.put(val,count++);
        }
    }

    PositiveBaseConverterEnum(int start, int total) {
        encodingArray = new String[total];
        mapIndexes= new HashMap<>();
        for(int count=0;count<total;count++){
            encodingArray[count] = generate(start+count);
            mapIndexes.put(encodingArray[count],count+1);
        }
    }

   protected String generate(int val){
       throw new UnsupportedOperationException("Operation not supported");
   }

    String encode(int value){
        if (value <= 0) {
            throw new IllegalArgumentException("Invalid Value");
        }
        String returnValue="";
        do{
            int reminder = (value%encodingArray.length);
            returnValue = encodingArray[reminder]+returnValue;
            value = value/encodingArray.length;
        }while (value>0);
        return returnValue;
    }

    int decode(String encoded){
        int returnValue = 0;
        while (encoded.length()>0){
            String prefix = encoded.substring(0,1);
            encoded = encoded.substring(1);
            returnValue = returnValue
                    +(mapIndexes.get(prefix)*(int)Math.pow(encodingArray.length,encoded.length()));
        }
        return returnValue;
    }

    protected String[] getEncodingArray() {
        return encodingArray;
    }

}

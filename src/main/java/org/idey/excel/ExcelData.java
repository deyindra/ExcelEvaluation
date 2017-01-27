package org.idey.excel;

import org.idey.excel.expression.IgnoreCoverage;

/**
 * @author i.dey
 * Class to Hold either Error message or {@link Double} value in excel cell
 */
public final class ExcelData{
    private String errorMessage;
    private Double data;

    /**
     * @param errorMessage error message in {@link String} format
     * @throws IllegalArgumentException in case errorMessage is null or empty
     */
    public ExcelData(String errorMessage) {
        if(errorMessage==null || ("").equals(errorMessage.trim())){
            throw new IllegalArgumentException("Invalid Error Message");
        }
        this.errorMessage = errorMessage.trim();
    }

    /**
     * @param data {@link Double} value
     * @throws IllegalArgumentException in case value is null or {@link Double#NaN}
     */
    public ExcelData(Double data) {
        if(data==null || data.equals(Double.NaN)){
            throw new IllegalArgumentException("Invalid Double value");
        }
        this.data = data;
    }

    /**
     *
     * @return true is data is null
     */
    public boolean isError(){
        return data==null;
    }

    @IgnoreCoverage
    public Double getData() {
        return data;
    }

    @IgnoreCoverage
    public String getErrorMessage() {
        return errorMessage;
    }

    @IgnoreCoverage
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExcelData data1 = (ExcelData) o;

        return (errorMessage != null ? errorMessage.equals(data1.errorMessage) : data1.errorMessage == null) && (data != null ? data.equals(data1.data) : data1.data == null);
    }

    @IgnoreCoverage
    @Override
    public int hashCode() {
        int result = errorMessage != null ? errorMessage.hashCode() : 0;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if(isError()){
            return errorMessage.trim();
        }else{
            return Double.toString(data);
        }
    }

}

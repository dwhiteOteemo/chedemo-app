package org.djw.db;

public class DBColumn {

	public DBColumn(){}
	String ColName;
	String ColType;
	String ColLabel;
	int ColPrecision;
	int colScale;
	
	public String getColName() {
		return ColName;
	}
	public void setColName(String colName) {
		ColName = colName;
	}
	public String getColType() {
		return ColType;
	}
	public void setColType(String colType) {
		ColType = colType;
	}
	public String getColLabel() {
		return ColLabel;
	}
	public void setColLabel(String colLabel) {
		ColLabel = colLabel;
	}
	public int getColPrecision() {
		return ColPrecision;
	}
	public void setColPrecision(int colPrecision) {
		ColPrecision = colPrecision;
	}
	public int getColScale() {
		return colScale;
	}
	public void setColScale(int colScale) {
		this.colScale = colScale;
	}


}

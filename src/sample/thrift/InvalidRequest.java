/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package sample.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-03-20")
public class InvalidRequest extends org.apache.thrift.TException implements org.apache.thrift.TBase<InvalidRequest, InvalidRequest._Fields>, java.io.Serializable, Cloneable, Comparable<InvalidRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("InvalidRequest");

  private static final org.apache.thrift.protocol.TField WHAT_HAPPENS_FIELD_DESC = new org.apache.thrift.protocol.TField("whatHappens", org.apache.thrift.protocol.TType.STRING, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new InvalidRequestStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new InvalidRequestTupleSchemeFactory();

  public java.lang.String whatHappens; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    WHAT_HAPPENS((short)1, "whatHappens");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // WHAT_HAPPENS
          return WHAT_HAPPENS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.WHAT_HAPPENS, new org.apache.thrift.meta_data.FieldMetaData("whatHappens", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(InvalidRequest.class, metaDataMap);
  }

  public InvalidRequest() {
  }

  public InvalidRequest(
    java.lang.String whatHappens)
  {
    this();
    this.whatHappens = whatHappens;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public InvalidRequest(InvalidRequest other) {
    if (other.isSetWhatHappens()) {
      this.whatHappens = other.whatHappens;
    }
  }

  public InvalidRequest deepCopy() {
    return new InvalidRequest(this);
  }

  @Override
  public void clear() {
    this.whatHappens = null;
  }

  public java.lang.String getWhatHappens() {
    return this.whatHappens;
  }

  public InvalidRequest setWhatHappens(java.lang.String whatHappens) {
    this.whatHappens = whatHappens;
    return this;
  }

  public void unsetWhatHappens() {
    this.whatHappens = null;
  }

  /** Returns true if field whatHappens is set (has been assigned a value) and false otherwise */
  public boolean isSetWhatHappens() {
    return this.whatHappens != null;
  }

  public void setWhatHappensIsSet(boolean value) {
    if (!value) {
      this.whatHappens = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case WHAT_HAPPENS:
      if (value == null) {
        unsetWhatHappens();
      } else {
        setWhatHappens((java.lang.String)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case WHAT_HAPPENS:
      return getWhatHappens();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case WHAT_HAPPENS:
      return isSetWhatHappens();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof InvalidRequest)
      return this.equals((InvalidRequest)that);
    return false;
  }

  public boolean equals(InvalidRequest that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_whatHappens = true && this.isSetWhatHappens();
    boolean that_present_whatHappens = true && that.isSetWhatHappens();
    if (this_present_whatHappens || that_present_whatHappens) {
      if (!(this_present_whatHappens && that_present_whatHappens))
        return false;
      if (!this.whatHappens.equals(that.whatHappens))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetWhatHappens()) ? 131071 : 524287);
    if (isSetWhatHappens())
      hashCode = hashCode * 8191 + whatHappens.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(InvalidRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetWhatHappens()).compareTo(other.isSetWhatHappens());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetWhatHappens()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.whatHappens, other.whatHappens);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("InvalidRequest(");
    boolean first = true;

    sb.append("whatHappens:");
    if (this.whatHappens == null) {
      sb.append("null");
    } else {
      sb.append(this.whatHappens);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class InvalidRequestStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public InvalidRequestStandardScheme getScheme() {
      return new InvalidRequestStandardScheme();
    }
  }

  private static class InvalidRequestStandardScheme extends org.apache.thrift.scheme.StandardScheme<InvalidRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, InvalidRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // WHAT_HAPPENS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.whatHappens = iprot.readString();
              struct.setWhatHappensIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, InvalidRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.whatHappens != null) {
        oprot.writeFieldBegin(WHAT_HAPPENS_FIELD_DESC);
        oprot.writeString(struct.whatHappens);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class InvalidRequestTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public InvalidRequestTupleScheme getScheme() {
      return new InvalidRequestTupleScheme();
    }
  }

  private static class InvalidRequestTupleScheme extends org.apache.thrift.scheme.TupleScheme<InvalidRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, InvalidRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetWhatHappens()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetWhatHappens()) {
        oprot.writeString(struct.whatHappens);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, InvalidRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.whatHappens = iprot.readString();
        struct.setWhatHappensIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}


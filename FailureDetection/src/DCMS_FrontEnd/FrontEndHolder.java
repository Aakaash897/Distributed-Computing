package DCMS_FrontEnd;

/**
* DCMS_FrontEnd/FrontEndHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from FrontEnd.idl
* Friday, August 4, 2017 10:55:50 PM EDT
*/

public final class FrontEndHolder implements org.omg.CORBA.portable.Streamable
{
  public DCMS_FrontEnd.FrontEnd value = null;

  public FrontEndHolder ()
  {
  }

  public FrontEndHolder (DCMS_FrontEnd.FrontEnd initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = DCMS_FrontEnd.FrontEndHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    DCMS_FrontEnd.FrontEndHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return DCMS_FrontEnd.FrontEndHelper.type ();
  }

}

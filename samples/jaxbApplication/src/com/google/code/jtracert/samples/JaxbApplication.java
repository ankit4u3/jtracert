package com.google.code.jtracert.samples;

import com.sun.xml.bind.v2.model.annotation.RuntimeInlineAnnotationReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

public class JaxbApplication {

     public int field;

    public static void main(String[] arguments) throws Exception {

        Shiporder order = new Shiporder();
        order.setOrderid("orderId");
        order.setOrderperson("orderDescription");

        Shiporder.Shipto destination = new Shiporder.Shipto();
        destination.setAddress("address");
        destination.setCity("city");
        destination.setCountry("country");
        destination.setName("name");

        order.setShipto(destination);

        Marshaller m = JAXBContext.newInstance(Shiporder.class).createMarshaller();

        m.marshal(order, new ByteArrayOutputStream(0));

       method5();

    }

    private static void method5() throws NoSuchFieldException {
        new RuntimeInlineAnnotationReader().getFieldAnnotation(Override.class, JaxbApplication.class.getField("field"), null);
    }


}

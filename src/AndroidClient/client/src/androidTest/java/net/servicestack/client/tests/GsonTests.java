//  Copyright (c) 2015 ServiceStack LLC. All rights reserved.

package net.servicestack.client.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import net.servicestack.client.tests.dto.*;

public class GsonTests extends ApplicationTestCase<Application> {

    public GsonTests() {
        super(Application.class);
    }

    public void test_Gson() {
        Log.i("LOG", "=========== HELLO JSON ============");

        String json = "{\n" +
                "  \"posts\": [\n" +
                "    {\n" +
                "      \"post\": {\n" +
                "        \"username\": \"John\",\n" +
                "        \"message\": \"I'm back\",\n" +
                "        \"time\": \"2010-5-6 7:00:34\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"post\": {\n" +
                "        \"username\": \"Smith\",\n" +
                "        \"message\": \"I've been waiting\",\n" +
                "        \"time\": \"2010-4-6 10:30:26\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Gson gson = new Gson();
        PostList list = gson.fromJson(json, PostList.class);

        Log.i("LOG", "JSON: " + gson.toJson(list));
    }

    public class PostList {
        private List<PostContainer> posts = new ArrayList<>();

        public List<PostContainer> getPostContainterList() {
            return posts;
        }
    }

    class PostContainer {
        Posts post;
        public Posts getPost() {
            return post;
        }
    }

    public class Posts {
        String message;
        String time;
        String username;
    }

    public static class dto
    {
        public static class Foo {
            Integer fooId;
            String fooName;
        }
        public static class Bar {
            Integer barId;
            String barName;
            Foo foo;
        }
    }

    class NestedPojo {
        dto.Foo foo;
        dto.Bar bar;
        public Integer version = 1;
    }

    class NestedList extends ArrayList<NestedPojo> {

    }
    public static interface MetadataTestChild
    {
        public String name = null;
//        ArrayList<MetadataTestNestedChild> results;
    }

    public void test_Can_serialize_nested_classes() {
        NestedPojo o = new NestedPojo();
        o.foo = new dto.Foo();
        o.foo.fooId = 1;
        o.foo.fooName = "foo";

        o.bar = new dto.Bar();
        o.bar.barId = 2;
        o.bar.barName = "bar";

        List<NestedPojo> list = new ArrayList<>();
        list.add(o);
        list.add(o);
        list.add(o);

        Gson gson = new Gson();
        Log.i("LOG", "JSON LIST: " + gson.toJson(list));

        Class a = NestedPojo.class;
    }

    public void test_Can_deserialize_Hello() {
        String json = "{\"Result\":\"World\"}\n";

        Gson gson = new Gson();

        HelloResponse response = gson.fromJson(json, HelloResponse.class);

        assertEquals("World", response.getResult());
    }
}

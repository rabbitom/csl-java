package net.erabbit;

/**
 * Created by ziv on 2017/9/15.
 */

public class JSON {
    public static String str = "[\n" +
            "  {\n" +
            "    \"id\": \"csl-data\",\n" +
            "    \"name\": \"command\",\n" +
            "    \"length\": 1,\n" +
            "    \"type\": \"index\",\n" +
            "    \"format\": \"int\",\n" +
            "    \"value\": [\n" +
            "      {\n" +
            "        \"value\": 0,\n" +
            "        \"id\": \"default-data\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"value\": 1,\n" +
            "        \"id\": \"battery-data\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"value\": 2,\n" +
            "        \"id\": \"item-name-data\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"value\": 3,\n" +
            "        \"id\": \"user-name-data\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"value\": 4,\n" +
            "        \"id\": \"config-data\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"value\": 5,\n" +
            "        \"id\": \"cur-temperature-data\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"value\": 6,\n" +
            "        \"id\": \"temperature-record-data\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"value\": 7,\n" +
            "        \"id\": \"update-time-data\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"battery-get\",\n" +
            "    \"name\": \"command\",\n" +
            "    \"length\": 1,\n" +
            "    \"type\": \"fixed\",\n" +
            "    \"format\": \"int\",\n" +
            "    \"value\": [1],\n" +
            "    \"as-template\": \"command\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"battery-data\",\n" +
            "    \"length\": 2,\n" +
            "    \"type\": \"combination\",\n" +
            "    \"value\": [\n" +
            "      {\n" +
            "        \"template\": \"command\",\n" +
            "        \"value\": [1]\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"battery-level\",\n" +
            "        \"length\": 1,\n" +
            "        \"type\": \"variable\",\n" +
            "        \"format\": \"int\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"item-name-set\",\n" +
            "    \"length\": 16,\n" +
            "    \"type\": \"combination\",\n" +
            "    \"as-template\": \"item-name\",\n" +
            "    \"value\": [\n" +
            "      {\n" +
            "        \"template\": \"command\",\n" +
            "        \"value\": [2]\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"name\",\n" +
            "        \"type\": \"variable\",\n" +
            "        \"length\": 15,\n" +
            "        \"format\": \"string\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"item-name-get\",\n" +
            "    \"template\": \"command\",\n" +
            "    \"value\": [2]\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"item-name-data\",\n" +
            "    \"template\": \"item-name\"\n" +
            "  },\n" +
            "\n" +
            "  {\n" +
            "    \"id\": \"user-name-set\",\n" +
            "    \"length\": 16,\n" +
            "    \"type\": \"combination\",\n" +
            "    \"as-template\": \"user-name\",\n" +
            "    \"value\": [\n" +
            "      {\n" +
            "        \"template\": \"command\",\n" +
            "        \"value\": [3]\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"username\",\n" +
            "        \"type\": \"variable\",\n" +
            "        \"length\": 15,\n" +
            "        \"format\": \"string\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"user-name-get\",\n" +
            "    \"template\": \"command\",\n" +
            "    \"value\": [3]\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"user-name-data\",\n" +
            "    \"template\": \"user-name\"\n" +
            "  },\n" +
            "\n" +
            "  {\n" +
            "    \"id\": \"config-set\",\n" +
            "    \"length\": 19,\n" +
            "    \"type\": \"combination\",\n" +
            "    \"as-template\": \"config\",\n" +
            "    \"value\": [\n" +
            "      {\n" +
            "        \"template\": \"command\",\n" +
            "        \"value\": [4]\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"production-date\",\n" +
            "        \"id\": \"date\",\n" +
            "        \"length\": 3,\n" +
            "        \"type\": \"combination\",\n" +
            "        \"as-template\": \"date\",\n" +
            "        \"value\": [\n" +
            "          {\n" +
            "            \"name\": \"year\",\n" +
            "            \"length\": 1,\n" +
            "            \"type\": \"variable\",\n" +
            "            \"format\": \"bcd\",\n" +
            "            \"as-template\": \"date-field\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"month\",\n" +
            "            \"template\": \"date-field\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"day\",\n" +
            "            \"template\": \"date-field\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"expiry-date\",\n" +
            "        \"template\": \"date\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"start-time\",\n" +
            "        \"type\": \"variable\",\n" +
            "        \"length\": 4,\n" +
            "        \"format\": \"int.le\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"default-measure-interval\",\n" +
            "        \"length\": 1,\n" +
            "        \"type\": \"variable\",\n" +
            "        \"format\": \"int\"\n" +
            "      },\n" +
            "\n" +
            "      {\n" +
            "        \"name\": \"cool-down-timers\",\n" +
            "        \"type\": \"array\",\n" +
            "        \"length\": 6,\n" +
            "        \"value\": [\n" +
            "          {\n" +
            "            \"id\": \"cool-down-timer\",\n" +
            "            \"length\": 2,\n" +
            "            \"type\": \"combination\",\n" +
            "            \"value\": [\n" +
            "              {\n" +
            "                \"name\": \"time\",\n" +
            "                \"length\": 1,\n" +
            "                \"type\": \"variable\",\n" +
            "                \"format\": \"int\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"interval\",\n" +
            "                \"length\": 1,\n" +
            "                \"type\": \"variable\",\n" +
            "                \"format\": \"int\"\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"unit\",\n" +
            "        \"type\": \"variable\",\n" +
            "        \"format\": \"int\",\n" +
            "        \"length\": 1\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"config-get\",\n" +
            "    \"template\": \"command\",\n" +
            "    \"value\": [4]\n" +
            "  },\n" +
            "\n" +
            "  {\n" +
            "    \"id\": \"config-data\",\n" +
            "    \"template\": \"config\"\n" +
            "  },\n" +
            "\n" +
            "\n" +
            "  {\n" +
            "    \"id\": \"cur-temperature-get\",\n" +
            "    \"template\": \"command\",\n" +
            "    \"value\": [5]\n" +
            "  },\n" +
            "\n" +
            "  {\n" +
            "    \"id\": \"cur-temperature-data\",\n" +
            "    \"length\": 2,\n" +
            "    \"type\": \"combination\",\n" +
            "    \"value\": [\n" +
            "      {\n" +
            "        \"template\": \"command\",\n" +
            "        \"value\": [5]\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"cur-temperature\",\n" +
            "        \"length\": 1,\n" +
            "        \"type\": \"variable\",\n" +
            "        \"format\": \"int\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "\n" +
            "  {\n" +
            "    \"id\": \"temperature-record-get\",\n" +
            "    \"template\": \"command\",\n" +
            "    \"value\": [6]\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"temperature-record-data\",\n" +
            "    \"length\": 17,\n" +
            "    \"type\": \"combination\",\n" +
            "    \"value\": [\n" +
            "      {\n" +
            "        \"template\": \"command\",\n" +
            "        \"value\": [6]\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"temperature-records\",\n" +
            "        \"length\": 16,\n" +
            "        \"type\": \"array\",\n" +
            "        \"value\": [\n" +
            "          {\n" +
            "            \"id\": \"temperature-record\",\n" +
            "            \"length\": 4,\n" +
            "            \"type\": \"combination\",\n" +
            "            \"value\": [\n" +
            "              {\n" +
            "                \"name\": \"time\",\n" +
            "                \"type\": \"variable\",\n" +
            "                \"format\": \"int.le\",\n" +
            "                \"length\": 3\n" +
            "              },\n" +
            "              {\n" +
            "                \"name\": \"temperature\",\n" +
            "                \"type\": \"variable\",\n" +
            "                \"format\": \"int\",\n" +
            "                \"length\": 1\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "\n" +
            "  {\n" +
            "    \"id\": \"update-time-set\",\n" +
            "    \"length\": 5,\n" +
            "    \"type\": \"combination\",\n" +
            "    \"as-template\": \"update-time\",\n" +
            "    \"value\": [\n" +
            "      {\n" +
            "        \"template\": \"command\",\n" +
            "        \"value\": [7]\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"time\",\n" +
            "        \"type\": \"variable\",\n" +
            "        \"length\": 4,\n" +
            "        \"format\": \"int.le\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "\n" +
            "  {\n" +
            "    \"id\": \"update-time-get\",\n" +
            "    \"template\": \"command\",\n" +
            "    \"value\": [7]\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"update-time-data\",\n" +
            "    \"template\": \"update-time\"\n" +
            "  },\n" +
            "\n" +
            "  {\n" +
            "    \"id\": \"clean-temperature-records\",\n" +
            "    \"template\": \"command\",\n" +
            "    \"value\": [8]\n" +
            "  },\n" +
            "\n" +
            "  {\n" +
            "    \"id\": \"reset-device\",\n" +
            "    \"template\": \"command\",\n" +
            "    \"value\": [9]\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"default-data\",\n" +
            "    \"template\": \"command\",\n" +
            "    \"value\": [0]\n" +
            "  }\n" +
            "]";

}

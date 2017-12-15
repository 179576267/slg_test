package com.douqu.game.core.util;

import com.bean.core.util.FileUtils;
import com.douqu.game.core.factory.ConstantFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by bean on 2017/7/21.
 */
public class EntityUtils {

    public static void createEntity(String filePath, String outPath, String clsName)
    {
        try {
            List<String> list = FileUtils.loadFile(filePath);
            String fieldStr = list.get(0);
            String typeStr = list.get(1);
            String remarkStr = list.get(2);
            String[] fieldAry = fieldStr.split(ConstantFactory.TABLE);
            String[] typeAry = typeStr.split(ConstantFactory.TABLE);
            String[] remarkAry = remarkStr.split(ConstantFactory.TABLE);
            StringBuilder buffer = new StringBuilder();
            buffer.append("public class ");
            buffer.append(clsName);
            buffer.append(" extends GameObject {\n");
            for(int i = 0; i < fieldAry.length; i++)
            {
                buffer.append("/** " + remarkAry[i] + " */\n");
                buffer.append("public ");
                if(typeAry[i].equals("int"))
                    buffer.append("int ");
                else
                    buffer.append("String ");
                buffer.append(fieldAry[i]);
                buffer.append(";\n");
            }
            buffer.append("}\n");
            FileUtils.writeFile(outPath + clsName + ".java", buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

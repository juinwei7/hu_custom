package me.hu_custom.util;

import org.bukkit.inventory.ItemStack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class log {
    public static boolean isAir(ItemStack item) {
    return item == null || item.getType().isAir();
}

        public static void log(String message,String type) {
            File f = new File("plugins/Hu_custom/" + type + "/" + new SimpleDateFormat("yyyy-MM").format(new Date()) + ".log");
            File theDir = new File("plugins/Hu_custom/" + type + "/" );
            if (!theDir.exists()){
                theDir.mkdirs();
            }

            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
                bw.append("[").append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())).append("] ").append(message).append("\n");
                bw.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
}

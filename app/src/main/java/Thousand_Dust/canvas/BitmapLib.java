package Thousand_Dust.canvas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import Thousand_Dust.luaj.LuaBitmap;
import luaj.LuaLong;
import luaj.LuaTable;
import luaj.LuaValue;
import luaj.ap;
import luaj.lib.TwoArgFunction;
import luaj.lib.VarArgFunction;

/**
 * @author Thousand-Dust
 */
public class BitmapLib extends TwoArgFunction {

    @Override
    public LuaValue a(LuaValue arg1, LuaValue env) {
        env.a("loadBitmap", new loadBitmap());
        LuaTable table = new LuaTable();
        table.a("setWidth", new setWidth());
        table.a("setHeight", new setHeight());
        table.a("getWH", new getWH());
        table.a("remove", new remove());

        if (LuaBitmap.s_metatable == null) {
            LuaTable mt = LuaValue.b(
                    new LuaValue[] { D, table});
            LuaBitmap.s_metatable = mt;
        }

        return env;
    }

    class loadBitmap extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            return LuaBitmap.valueOf(BitmapFactory.decodeFile(args.r(1)));
        }
    }

    class setWidth extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            Bitmap bitmap = LuaBitmap.checkbitmap(args.c(1));
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(width/(float)args.m(2), 1);
            return LuaBitmap.valueOf(Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true));
        }
    }

    class setHeight extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            Bitmap bitmap = LuaBitmap.checkbitmap(args.c(1));
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(1, height/(float)args.m(2));
            return LuaBitmap.valueOf(Bitmap.createBitmap(bitmap, 0,0, width, height, matrix, true));
        }
    }

    class getWH extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            Bitmap bitmap = LuaBitmap.checkbitmap(args.c(1));
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            return LuaValue.b(new LuaValue[]{ LuaLong.b(width), LuaLong.b(height) });
        }
    }

    class remove extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            Bitmap bitmap = LuaBitmap.checkbitmap(args.c(1));
            bitmap.recycle();
            return x;
        }
    }

}

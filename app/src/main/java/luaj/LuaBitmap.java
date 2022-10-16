package luaj;

import android.graphics.Bitmap;

public class LuaBitmap extends LuaValue {

    private Bitmap bitmap;
    public static LuaValue s_metatable;

    @Override
    public int e_() {
        return 11;
    }

    @Override
    public String f_() {
        return "bitmap";
    }

    @Override
    public LuaValue i() {
        return s_metatable;
    }

    public static LuaBitmap valueOf(Bitmap bitmap){
        return new LuaBitmap(bitmap);
    }

    private LuaBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public static Bitmap checkbitmap(LuaValue luaValue) {
        return luaValue instanceof LuaBitmap ? ((LuaBitmap) luaValue).bitmap : null;
    }
}

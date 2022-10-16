package Thousand_Dust.luaj;

import Thousand_Dust.DrawView;
import luaj.LuaValue;

public class LuaView extends LuaValue {

    private DrawView view;
    public static LuaValue s_metatable;
    
    @Override
    public int e_() {
        return 10;
    }

    @Override
    public String f_() {
        return "view";
    }

    @Override
    public LuaValue i() {
        return s_metatable;
    }
    
    public static LuaView valueOf(DrawView v){
        return new LuaView(v);
    }
    
    private LuaView(DrawView view){
        this.view = view;
    }

    public static DrawView checkview(LuaValue luaValue) {
        return luaValue instanceof LuaView ? ((LuaView) luaValue).view : null;
    }
}

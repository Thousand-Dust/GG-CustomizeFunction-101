package android.canvas;

import android.os.Build;
import android.pro.DrawView;
import android.pro.MyWindowManager;
import android.pro.Tools;

import luaj.Globals;
import luaj.LuaView;
import luaj.lib.TwoArgFunction;
import luaj.LuaValue;
import luaj.LuaTable;
import luaj.lib.VarArgFunction;
import luaj.ap;

/**
 * @author Thousand-Dust
 */
public class ViewLib extends TwoArgFunction {

    private Globals globals;

    public ViewLib() {
        //android版本小于12直接显示悬浮窗
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            MyWindowManager.newInstance(Tools.getContext());
        }
    }

    @Override
    public LuaValue a(LuaValue arg1, LuaValue env) {
        globals = env.c();
        env.a("newView", new newView());
        env.a("removeAllView", new removeAllView());

        LuaTable view = new LuaTable();
        //调用Lua绘制函数更新绘制
        view.a("invalidate", new invalidate());
        //显示绘制
        view.a("show", new showDraw());
        //删除绘制内容
        view.a("close", new closeDraw());

        if (!env.j("package").F()) {
            env.j("package").j("loaded").a("view", view);
        }
        if (LuaView.s_metatable == null) {
            LuaTable mt = LuaValue.b(
                    new LuaValue[] { D, view});
            LuaView.s_metatable = mt;
        }

        return env;
    }

    class newView extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            DrawView drawView = new DrawView(Tools.getContext(), globals);
            MyWindowManager.getInstance().addView(drawView);
            return LuaView.valueOf(drawView);
        }
    }

    class invalidate extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaView.checkview(args.c(1)).postInvalidate();
            return x;
        }
    }

    class showDraw extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            DrawView luaView = LuaView.checkview(args.c(1));
            luaView.setDrawFun(args.n(2));
            luaView.start(args.d(3, 60));
            return LuaValue.x;
        }
    }

    class closeDraw extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            DrawView luaView = LuaView.checkview(args.c(1));
            luaView.close();
            MyWindowManager.getInstance().removeView(luaView);
            return LuaValue.x;
        }
    }

    private class removeAllView extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            MyWindowManager.getInstance().removeAllViews();
            return LuaValue.x;
        }
    }
}

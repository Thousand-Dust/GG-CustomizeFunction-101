package android.canvas;
import luaj.LuaPaint;
import luaj.LuaString;
import luaj.lib.TwoArgFunction;
import luaj.LuaValue;
import luaj.lib.VarArgFunction;
import luaj.LuaTable;
import luaj.ap;
import android.graphics.Paint;
import luaj.o;
import android.graphics.Color;

/**
 * @author Thousand-Dust
 */
public class PaintLib extends TwoArgFunction {

    @Override
    public LuaValue a(LuaValue arg1, LuaValue env) {

        env.a("newPaint", new newPaint());

        LuaTable paint = new LuaTable();
        //设置画笔笔触宽
        paint.a("setWidth", new setWidth());
        //设置画笔样式
        paint.a("setStyle", new setStyle());
        //设置画笔颜色
        paint.a("setColor", new setColor());
        //设置画笔画出的字符大小
        paint.a("setTextSize", new setTextSize());
        //设置抗锯齿
        paint.a("setAntiAlias", new setAntiAlias());

        if (!env.j("package").F()) {
            env.j("package").j("loaded").a("paint", paint);
        }
        if (LuaPaint.s_metatable == null) {
            LuaTable mt = LuaValue.b(
                    new LuaValue[] { D, paint});
            LuaPaint.s_metatable = mt;
        }

        return env;
    }

    class newPaint extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            return LuaPaint.valueOf(new Paint());
        }
    }

    class setWidth extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaPaint.checkpaint(args.c(1)).setStrokeWidth(args.o(2));
            return x;
        }
    }

    class setStyle extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaValue value = args.v(2);

            Paint.Style style;
            if (value instanceof LuaString) {
                String styleStr = value.y();
                switch (styleStr) {
                    case "描边":
                        style = Paint.Style.STROKE;
                        break;
                    case "填充":
                        style = Paint.Style.FILL;
                        break;
                    case "描边并填充":
                        style = Paint.Style.FILL_AND_STROKE;
                        break;

                    default:
                        throw new o("未知的画笔类型");
                }
            } else {
                int styleNum = value.v();
                switch (styleNum) {
                    case 0:
                        //描边
                        style = Paint.Style.STROKE;
                        break;
                    case 1:
                        //填充
                        style = Paint.Style.FILL;
                        break;
                    case 2:
                        //描边并填充
                        style = Paint.Style.FILL_AND_STROKE;
                        break;

                    default:
                        throw new o("未知的画笔类型");
                }
            }

            LuaPaint.checkpaint(args.c(1)).setStyle(style);

            return x;
        }
    }

    class setColor extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaPaint.checkpaint(args.c(1)).setColor(Color.parseColor(args.r(2)));
            return x;
        }
    }

    class setTextSize extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaPaint.checkpaint(args.c(1)).setTextSize((float)args.m(2));
            return x;
        }
    }

    class setAntiAlias extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaPaint.checkpaint(args.c(1)).setAntiAlias(args.k(2));
            return x;
        }
    }
}

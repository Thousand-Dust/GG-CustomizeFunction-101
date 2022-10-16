package android.canvas;

import luaj.LuaBitmap;
import luaj.LuaCanvas;
import luaj.LuaFunction;
import luaj.LuaPaint;
import luaj.lib.TwoArgFunction;
import luaj.LuaValue;
import luaj.LuaTable;
import luaj.lib.VarArgFunction;

import android.graphics.Canvas;

import luaj.ap;

import android.graphics.RectF;
import android.graphics.Paint;

import luaj.o;

import android.graphics.Color;

/**
 * @author Thousand-Dust
 */
public class CanvasLib extends TwoArgFunction {

    @Override
    public LuaValue a(LuaValue modname, LuaValue env) {
        LuaTable canvas = new LuaTable();
        //绘制圆弧
        canvas.a("drawArc", new drawArc());
        //绘制Bitmap图片
        canvas.a("drawBitmap", new drawBitmap());
        //绘制圆
        canvas.a("drawCircle", new drawCircle());
        //绘制背景颜色
        canvas.a("drawColor", new drawColor());
        //绘制线段
        canvas.a("drawLine", new drawLine());
        //绘制多条线段
        canvas.a("drawLines", new drawLines());
        //绘制四边形
        canvas.a("drawRect", new drawRect());
        //绘制字符串
        canvas.a("drawText", new drawText());
        //裁剪绘制内容（四边形）
        canvas.a("clipRect", new clipRect());
        //旋转画布
        canvas.a("rotate", new rotate());
        //移动画布原点
        canvas.a("translate", new translate());
        //保存画布状态
        canvas.a("save", new Save());
        //恢复上次保存的画布状态
        canvas.a("restore", new Restore());

        if (!env.j("package").F()) {
            env.j("package").j("loaded").a("canvas", canvas);
        }
        if (LuaCanvas.s_metatable == null) {
            LuaTable mt = LuaValue.b(
                    new LuaValue[] { D, canvas});
            LuaCanvas.s_metatable = mt;
        }

        return env;
    }

    class drawArc extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaTable table = args.t(2);
            RectF rectF = new RectF((float)table.m(1), (float)table.m(2), (float)table.m(3), (float)table.m(4));
            LuaCanvas.checkcanvas(args.c(1))
                    .drawArc(rectF, (float)args.m(3), (float)args.m(4), args.k(5), LuaPaint.checkpaint(args.c(6)));
            return x;
        }
    }

    class drawBitmap extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaCanvas.checkcanvas(args.c(1)).drawBitmap(LuaBitmap.checkbitmap(args.c(2)), (float)args.m(3), (float)args.m(4), LuaPaint.checkpaint(args.c(5)));
            return x;
        }
    }

    class drawCircle extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaCanvas.checkcanvas(args.c(1))
                    .drawCircle((float)args.m(2), (float)args.m(3), (float)args.m(4), LuaPaint.checkpaint(args.c(5)));
            return x;
        }
    }

    class drawColor extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaCanvas.checkcanvas(args.c(1)).drawColor(Color.parseColor(args.r(2)));
            return x;
        }
    }

    class drawLine extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaCanvas.checkcanvas(args.c(1))
                    .drawLine((float)args.m(2), (float)args.m(3), (float)args.m(4), (float)args.m(5), LuaPaint.checkpaint(args.c(6)));
            return x;
        }
    }

    class drawLines extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaTable table = args.t(2);
            float[] floats = new float[table.L()];
            for (int i = 0; i < floats.length; i++) {
                floats[i] = (float)table.c_(i).x();
            }
            LuaCanvas.checkcanvas(args.c(1)).drawLines(floats, LuaPaint.checkpaint(args.c(3)));
            return x;
        }
    }

    class drawProgress extends VarArgFunction {
        private Paint framePaint;
        private Paint progressPaint;

        public drawProgress(Paint paint) {
            this.framePaint = paint;
            progressPaint = new Paint();
            progressPaint.setStyle(Paint.Style.FILL);
        }

        @Override
        public ap a_(ap args) {
            int narg = args.j_();
            if (narg >= 6) {
                framePaint.setColor(Color.parseColor(args.r(6))); //使用用户自定义背景颜色
            } else {
                framePaint.setColor(Color.parseColor("#FFFFFF"));
                //判断是否使用用户自定义进度颜色
                if (narg == 5) {
                    progressPaint.setColor(Color.parseColor(args.r(5)));
                } else {
                    progressPaint.setColor(Color.parseColor("#FF0000"));
                }
            }
            Canvas canvas = LuaCanvas.checkcanvas(args.c(1)); //使用默认画笔
            //获得进度条背景矩形
            float left, top, right, bottom;
            {
                LuaTable table = args.t(2);
                left = (float) table.c_(1).x();
                top = (float) table.c_(2).x();
                right = (float) table.c_(3).x();
                bottom = (float) table.c_(4).x();
            }
            RectF frameRect = new RectF(left, top, right, bottom);
            //绘制进度条背景
            canvas.drawRect(frameRect, framePaint);
            //计算进度矩形
            float max = (float)args.m(3);
            float progress = (float)args.m(4);
            float proportion = (right - left) / max;
            RectF internel = new RectF(left, top, left + (progress * proportion), bottom);
            //绘制进度
            canvas.drawRect(internel, progressPaint);

            return x;
        }
    }

    class drawRect extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaTable table = args.t(2);
            RectF rectF = new RectF((float)table.c_(1).x(), (float)table.c_(2).x(), (float)table.c_(3).x(), (float)table.c_(4).x());
            LuaCanvas.checkcanvas(args.c(1)).drawRect(rectF, LuaPaint.checkpaint(args.c(3)));
            return x;
        }
    }

    class drawText extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaCanvas.checkcanvas(args.c(1))
                    .drawText(args.r(2), (float)args.m(3), (float)args.m(4), LuaPaint.checkpaint(args.c(5)));
            return x;
        }
    }

    class clipRect extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaTable table = args.t(2);
            LuaCanvas.checkcanvas(args.c(1)).clipRect(new RectF((float)table.m(1), (float)table.m(2), (float)table.m(3), (float)table.m(4)));
            return x;
        }
    }

    class rotate extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            Canvas canvas = LuaCanvas.checkcanvas(args.c(1));
            switch (args.j_()) {
                case 2:
                    canvas.rotate((float)args.m(2));
                    break;
                case 4:
                    canvas.rotate((float)args.m(2), (float)args.m(3), (float)args.m(4));
                    break;
                default:
                    throw new o("there is no such method: " + args);
            }
            return x;
        }
    }

    class translate extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaCanvas.checkcanvas(args.c(1)).translate((float)args.m(2), (float)args.m(3));
            return x;
        }
    }

    class Save extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaCanvas.checkcanvas(args.c(1)).save();
            return x;
        }
    }

    class Restore extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            LuaCanvas.checkcanvas(args.c(1)).restore();
            return x;
        }
    }

}
package Thousand_Dust.canvas;

import static Thousand_Dust.MyWindowManager.MAXIMUM_OBSCURING_OPACITY;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import Thousand_Dust.DevInfo;
import Thousand_Dust.DrawView;
import Thousand_Dust.MyWindowManager;
import Thousand_Dust.Tools;

import luaj.Globals;
import Thousand_Dust.luaj.LuaView;
import luaj.lib.TwoArgFunction;
import luaj.LuaValue;
import luaj.LuaTable;
import luaj.lib.VarArgFunction;
import luaj.ap;
import luaj.o;

/**
 * @author Thousand-Dust
 */
public class ViewLib extends TwoArgFunction {

    private Globals globals;

    @Override
    public LuaValue a(LuaValue arg1, LuaValue env) {
        globals = env.c();
        //强制关闭绘图无障碍
        env.a("disDrawAcc", new disDrawAcc());

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

    class disDrawAcc extends VarArgFunction {
        @Override
        public ap a_(ap args) {
            MyWindowManager.setAlpha(MAXIMUM_OBSCURING_OPACITY);
            return LuaValue.x;
        }
    }

    class newView extends VarArgFunction {
        private boolean isPrint = false;
        @Override
        public ap a_(ap args) {
            if (MyWindowManager.isInstanceEmpty()) {
                //android版本12及以上需要开启无障碍
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && MyWindowManager.getAlpha() > MAXIMUM_OBSCURING_OPACITY) {
                    showPermissionAlert();
                } else {
                    showOrdinaryAlert();
                }
            }
            DrawView drawView = new DrawView(Tools.getContext(), globals);
            MyWindowManager.getInstance().addView(drawView);
            return LuaView.valueOf(drawView);
        }
        public void showOrdinaryAlert() {
            final boolean[] result = {false, false};
            Runnable run = () -> {
                //复制函数开发者信息可点击文字
                TextView devText = new TextView(Tools.getContext());
                devText.setText("输出并复制函数开发者信息");
                devText.setTextColor(Color.parseColor("#00FFFF"));
                devText.setGravity(Gravity.CENTER_HORIZONTAL);
                devText.setPadding(0, 20, 0, 0);
                devText.setOnClickListener((v) -> {
                    android.ext.Tools.a(DevInfo.getAuthor());
                    if (!isPrint) {
                        globals.e.println(DevInfo.getAuthor());
                        isPrint = true;
                    }
                });

                //绘制提示弹窗
                final AlertDialog.Builder ab = new AlertDialog.Builder(Tools.getContext());
                ab.setTitle("绘制");
                ab.setMessage("脚本即将在屏幕上绘图\n同意继续，拒绝则退出脚本。");
                ab.setView(devText);
                ab.setCancelable(false);
                ab.setPositiveButton("同意", (p1, p2) -> {
                    MyWindowManager.newInstance(Tools.getContext());
                    result[1] = true;
                });
                ab.setNegativeButton("拒绝", (p1, p2) -> {
                    result[1] = false;
                });
                ab.setOnDismissListener((dialogInterface) -> result[0] = true);
                final AlertDialog dialog = ab.create();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                } else {
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                }
                dialog.show();
            };

            new Handler(Looper.getMainLooper()).post(run);
            while (!result[0]) {
                Thread.yield();
            }
            //拒绝在屏幕上绘制
            if (!result[1]) {
                throw new o("用户拒绝在屏幕上绘制");
            }
        }
        public void showPermissionAlert() {
            final boolean[] isClock = {false};
            Runnable run = () -> {
                //复制函数开发者信息可点击文字
                TextView devText = new TextView(Tools.getContext());
                devText.setText("输出并复制函数开发者信息");
                devText.setTextColor(Color.parseColor("#00FFFF"));
                devText.setGravity(Gravity.CENTER_HORIZONTAL);
                devText.setPadding(0, 20, 0, 0);
                devText.setOnClickListener((v) -> {
                    android.ext.Tools.a(DevInfo.getAuthor());
                    if (!isPrint) {
                        globals.e.println(DevInfo.getAuthor());
                        isPrint = true;
                    }
                });

                //权限请求弹窗
                final AlertDialog.Builder ab = new AlertDialog.Builder(Tools.getContext());
                ab.setTitle("权限请求");
                ab.setMessage("脚本即将在屏幕上绘图\n" +
                        "但因为您的Android设备版本为12或以上，需要开启无障碍（或调用函数\"disDrawAcc()\"）\n" +
                        "同意跳转权限页面，拒绝结束脚本。");
                ab.setView(devText);
                ab.setCancelable(false);
                ab.setPositiveButton("同意", (p1, p2) -> {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    Tools.getContext().startActivity(intent);
                });
                ab.setNegativeButton("拒绝", null);
                ab.setOnDismissListener((dialogInterface) -> isClock[0] = true);
                final AlertDialog dialog = ab.create();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                } else {
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                }
                dialog.show();
            };

            new Handler(Looper.getMainLooper()).post(run);
            while (!isClock[0]) {
                Thread.yield();
            }
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

package com.chentao.designmodestudy.project1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 程序设计应该遵守几大原则:
 * 1.单一原则：一个类尽量主要负责一种职责，这样每个类逻辑更加单一、代码更加简洁
 * <p>
 * 2.开闭原则：对于扩展时开放的，对于修改是封闭的。产品升级过程中对原有代码升级、修改时，可能会将错误引入原来已经
 * 测试好旧代码中，破坏原有系统。因此，代码需要变化时，应尽量通过扩展方式，当然不可避免会修改原来代码
 * 重点：开闭原则的重要手段应该时通过抽象
 *
 * 4.依赖倒置原则：定义是：使得高层次的模块不依赖于低层次的细节。
 * 依赖倒置原则是一种特定的解耦形式
 *
 * 依赖倒置原则的关键点：
 * 1.高层模块不应该依赖低层模块，两者都应该依赖其抽象
 * 2.抽象不应该依赖细节
 * 3.细节应该依赖抽象
 *
 * java中，抽象就是指接口或抽象类,细节就是实现类。模块间的依赖通过抽象发生，实现类之间不发生直接的
 * 依赖关系，其依赖关系是通过接口或抽象类产生的。
 *
 * 总结：依赖倒置原则就是高层调用时，是调用抽象（接口或抽象类），二不是调用抽象的实现（接口或抽象类
 * 的实现类）,这样抽象可以向下兼容它的实现类。
 *
 * 要想让系统变得更灵活，抽象是一种高效手段
 * 如ImageLoader中，引用的是ImageCache类，而不是直接引用它的实现类，DiskCache、MemoryCache等
 *
 *  5.接口隔离原则：定义是：客户端不应该依赖它不需要的接口，或者说类间的依赖关系应该建立在最小的接口
 *  上。
 *
 *  总结：接口隔离原则原理就是将大的接口拆分成更小的和更具体的接口
 *
 *  目的是解开系统耦合，从而容易重构、更改和重新部署
 *
 */
public class ImageLoader {

    // 这使用ImageCache依赖于抽象，提供一个默认实现
    ImageCache mImageCache = new MemoryCache();

    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 这种设置接口的方法叫做依赖注入
     * @param cache
     */
    public void setImageCache(ImageCache cache){
        mImageCache = cache;
    }

    public void displayImage(final String url, final ImageView imageView) {
        Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        submitLoadRequest(url,imageView);

    }

    private void submitLoadRequest(final String url, final ImageView imageView){
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downLoadImage(url);
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    updateImageView(imageView, bitmap);
                }
                mImageCache.put(url,bitmap);
            }
        });
    }

    private void updateImageView(final ImageView imageView, final Bitmap bitmap) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    public Bitmap downLoadImage(String url) {
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        try {
            URL url1 = new URL(url);
            connection = (HttpURLConnection) url1.openConnection();
            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return bitmap;
    }

}

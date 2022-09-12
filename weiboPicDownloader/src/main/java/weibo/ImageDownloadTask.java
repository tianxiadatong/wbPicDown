package weibo;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class ImageDownloadTask implements Runnable{
    private CountDownLatch downLatch;
    private int imageIndex;
    private String imageUrl;

    private List<String> imageUrls;

    public int getImageIndex() {
        return imageIndex;
    }
    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public ImageDownloadTask(CountDownLatch downLatch, int imageIndex, String imageUrl, List<String> imageUrls) {
        this.downLatch = downLatch;
        this.imageIndex = imageIndex;
        this.imageUrl = imageUrl;
        this.imageUrls = imageUrls;
    }



    @Override
    public void run() {
        try{
            System.out.println("下载图片: " + ( imageIndex + 1));
            byte[] imgBytes = FileUtils.download(imageUrl, 100_000);
            if(Objects.isNull(imgBytes)){
                System.out.println(imgBytes);
                imageUrls.add(imageUrl);
            }
            FileUtils.byte2File(imgBytes, WeiboDownloader.IMG_LOCATION, imageIndex+1+getSuffix(imageUrl));
        }catch (Exception e) {
        }finally {
            downLatch.countDown();
        }
    }
    private String getSuffix(String url){
        if(!url.substring(url.lastIndexOf("/")).contains(".")){
            return ".jpg";
        }
        try{
            return url.substring(url.lastIndexOf("."));
        }catch(Exception e){
            e.printStackTrace();
        }
        return ".jpg";
    }
}

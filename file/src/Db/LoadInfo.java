package Db;
public class LoadInfo {

        public int fileSize;// �ļ���С
        private int complete;// ��ɶ�
        private String urlstring;// ��������ʶ
        public LoadInfo() {
                super();
                // TODO Auto-generated constructor stub
        }
        public LoadInfo(int fileSize, int complete, String urlstring) {
                super();
                this.fileSize = fileSize;
                this.complete = complete;
                this.urlstring = urlstring;
        }
        public int getFileSize() {
                return fileSize;
        }
        public void setFileSize(int fileSize) {
                this.fileSize = fileSize;
        }
        public int getComplete() {
                return complete;
        }
        public void setComplete(int complete) {
                this.complete = complete;
        }
        public String getUrlstring() {
                return urlstring;
        }
        public void setUrlstring(String urlstring) {
                this.urlstring = urlstring;
        }
        @Override
            public String toString() {
         return "LoadInfo [fileSize=" + fileSize + ", complete=" + complete
                         + ", urlstring=" + urlstring + "]";
     }


}

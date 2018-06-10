//package Client;
//
//public class Start extends Runnable {
//    @Override
//        public void run() {
//            while(true) {
//
//                try {
//                    Thread.sleep(0);
//                } catch (InterruptedException e) {}
//                if (resolution==true) {
//                    int ind=20;
//                    while (ind>0) {
//                        if (resolution==false) {
//                            while(true) {
//                                try {
//                                    Thread.sleep(0);
//                                } catch (InterruptedException e) {}
//                                if (resolution==true)
//                                    break;
//                            }
//                        }
//                        repaint();
//                        ind--;
//                        del=del-1;
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException ex) {}
//                    }
//                    while (ind<20) {
//                        if (resolution==false) {
//                            while(true) {
//                                try {
//                                    Thread.sleep(0);
//                                } catch (InterruptedException e) {}
//                                if (resolution==true)
//                                    break;
//                            }
//                        }
//                        repaint();
//                        ind++;
//                        del=del+1;
//                        try {
//                            Thread.sleep(200);
//                        } catch (InterruptedException ex) {}
//                    }
//                    resolution=false;
//                }
//            }
//    }
//}

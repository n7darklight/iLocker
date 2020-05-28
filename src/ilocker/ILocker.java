/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ilocker;

/**
 *
 * Nana Mulyana Maghfur
 * 1810631170171
 */

//import necesery library
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.LineBorder;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.SwingUtilities;
import com.google.zxing.BinaryBitmap; 
import com.google.zxing.MultiFormatReader; 
import com.google.zxing.Result; 
import com.google.zxing.client.j2se.BufferedImageLuminanceSource; 
import com.google.zxing.common.HybridBinarizer; 
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ILocker extends JFrame implements Runnable, ThreadFactory{  
    //global declaration
    //command string
    final String HINT = "HINT";
    final String HINT1 = "HINT1";
    final String NEXT = "NEXT";
    final String BACK = "BACK";
    final String FIRST = "FIRST";
    final String WARN1 = "WARN1";
    final String WARN2 = "WARN2";
    final String NO = "NO";
    final String EXIT = "EXIT";
    final String LOGOUT = "LOGOUT";
    
    //base component
    private WebcamPanel panel = null;
    private Webcam webcam = null;
    private JFrame frame = new JFrame("ilocker");
    private JPanel card1 = new JPanel();
    private JPanel card2 = new JPanel();
    private JLayeredPane card3 = new JLayeredPane();
    private JPanel cards = new JPanel(new CardLayout());
    private static final long serialVersionUID = 6441489157408381878L;
    private Executor executor = Executors.newSingleThreadExecutor(this);
    
    //database access
    String url = "jdbc:mysql://localhost:3306/suryakencana?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    String user = "root";
    String pass = "";

    //function button initiating
    File file7 = new File("src/pinjam.png");
    File file8 = new File("src/buka.png");
    File file9 = new File("src/logout.png");
    File file10 = new File("src/close.png");
    File file11 = new File("src/ya.png");
    File file12 = new File("src/tidak.png");
    //pinjam button initiating
    BufferedImage pinjamIcon;
    JButton pinjamButton = new JButton();
    //buka button initiating
    BufferedImage bukaIcon; 
    JButton bukaButton = new JButton();
    //logout button initiating
    BufferedImage logoutIcon = null;
    JButton logoutButton = new JButton();
    //close popup button initiating
    BufferedImage closeIcon;
    JButton close = new JButton();
    //yes popup button initiating
    BufferedImage yesIcon;
    JButton yes = new JButton();
    //no popup button initiating
    BufferedImage noIcon;
    JButton no = new JButton();
        
    //miscelinous component
    JTextPane wel = new JTextPane();//welcome with name textpane
    JLabel stat = new JLabel();//status label
    JPanel pop = new JPanel();//popup panel
    JLabel mas = new JLabel();//popup message
    JLabel nolocker = new JLabel();//locker no
    File file13 = new File("src/inactive.png");
    File file14 = new File("src/active.png");
    JLabel stImage = new JLabel();
    BufferedImage nactIcon;
    BufferedImage actIcon;
    
    //class constructor
    public ILocker() throws IOException{
        this.pinjamIcon = ImageIO.read(file7);
        this.bukaIcon = ImageIO.read(file8);
        this.logoutIcon = ImageIO.read(file9);
        this.closeIcon = ImageIO.read(file10);
        this.yesIcon = ImageIO.read(file11);
        this.noIcon = ImageIO.read(file12);
        this.nactIcon = ImageIO.read(file13);
        this.actIcon = ImageIO.read(file14);
        this.pinjamButton.setIcon(new ImageIcon(pinjamIcon));
        this.bukaButton.setIcon(new ImageIcon(bukaIcon));
        this.logoutButton.setIcon(new ImageIcon(logoutIcon));
        this.close.setIcon(new ImageIcon(closeIcon));
        this.yes.setIcon(new ImageIcon(yesIcon));
        this.no.setIcon(new ImageIcon(noIcon));
        pop.add(mas);
        pop.add(nolocker);
        pop.add(close);
        pop.add(yes);
        pop.add(no);
        initComponents();
    }
    
    //method to make connection with database
    public Connection connectdb(){
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ILocker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
    
    //method for showing popup message
    public void popup(String a){
        pop.setLayout(null);
        ControlAction c = new ControlAction();
        pop.setBounds(420, 320, 460, 160);
        pop.setBorder(new LineBorder(Color.BLACK, 2));
        //condition for showing popup message
        if(a.startsWith("1")){
            mas.setText("Selamat Anda Telah Melakukan Peminjaman");
            nolocker.setText("No. Locker Anda Yaitu 17");
            yes.setVisible(false);
            no.setVisible(false);
            nolocker.setVisible(true);
        }else if(a.equals(WARN1)){
            mas.setText("Maaf Anda Telah Melakukan Peminjaman");
            yes.setVisible(false);
            no.setVisible(false);
            nolocker.setVisible(false);
        }else if(a.equals(WARN2)){
            mas.setText("Maaf Anda Belum Melakukan Peminjaman");
            yes.setVisible(false);
            no.setVisible(false);
            nolocker.setVisible(false);
        }else if(a.contains("BUKA")){
            mas.setText("Anda Ingin Mengakhiri Peminjaman ?");
            yes.setVisible(true);
            String key = a.substring(4);
            yes.setActionCommand("YES"+key);
            no.setVisible(true);
            nolocker.setVisible(false);
        }else if(a.equals(NO)){
            mas.setText("Loker Anda Telah Terbuka");
            yes.setVisible(false);
            no.setVisible(false);
            nolocker.setVisible(false);
        }    
        mas.setFont(new Font("arial", Font.BOLD, 20));
        mas.setOpaque(false);
        mas.setForeground(Color.BLACK);
        Dimension si1 = mas.getPreferredSize();
        mas.setBounds(10, 40, si1.width, si1.height);
        nolocker.setFont(new Font("arial", Font.BOLD, 20));
        nolocker.setOpaque(false);
        nolocker.setForeground(Color.BLACK);
        Dimension si3 = nolocker.getPreferredSize();
        nolocker.setBounds(10, 70, si3.width, si3.height);
        //close button handling
        close.addActionListener(c);
        close.setActionCommand(EXIT);
        Dimension si2 = close.getPreferredSize();
        close.setBounds(420, 10, si2.width, si2.height);
        close.setBorder(BorderFactory.createEmptyBorder());
        close.setContentAreaFilled(false);
        //yes button handling
        yes.addActionListener(c);
        Dimension si4 = yes.getPreferredSize();
        yes.setBounds(100, 100, si4.width, si4.height);
        yes.setBorder(BorderFactory.createEmptyBorder());
        yes.setContentAreaFilled(false);
        //no button handling
        no.addActionListener(c);
        no.setActionCommand(NO);
        Dimension si5 = no.getPreferredSize();
        no.setBounds(250, 100, si5.width, si5.height);
        no.setBorder(BorderFactory.createEmptyBorder());
        no.setContentAreaFilled(false);
        //add to card3 top most layer
        card3.add(pop,6,0);
    } 
    
    //action listeneer for functionality button
    class ControlAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CardLayout cl = (CardLayout) (cards.getLayout());
            String cmd = e.getActionCommand();
            if (cmd.equals(NEXT)) {
                cl.next(cards);
            }else if (cmd.startsWith("1")) {
                try {
                    Connection con = connectdb();
                    Statement st = con.createStatement();
                    st.executeUpdate("UPDATE pegawai SET StatusPinjaman=1 WHERE idpegawai="+cmd);
                    Statement st1 = con.createStatement();
                    ResultSet a = st1.executeQuery("SELECT StatusPinjaman FROM pegawai WHERE idpegawai="+cmd);
                    a.next();
                    int b = a.getInt("StatusPinjaman");
                    cekst(b,stat);
                    ControlAction ca = new ControlAction();
                    //button action
                    if(b==0){
                        pinjamButton.addActionListener(ca);
                        pinjamButton.setActionCommand(cmd);
                        bukaButton.addActionListener(ca);
                        bukaButton.setActionCommand(WARN2);
                    }else{
                        pinjamButton.addActionListener(ca);
                        pinjamButton.setActionCommand(WARN1);
                        bukaButton.addActionListener(ca);
                        bukaButton.setActionCommand("BUKA"+cmd);
                    }
                    popup(cmd);
                } catch (SQLException ex) {
                    Logger.getLogger(ILocker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(cmd.equals(WARN1)){
                popup(cmd);
            }else if(cmd.equals(WARN2)){
                popup(cmd);
            }else if(cmd.contains("BUKA")){
                popup(cmd);
            }else if(cmd.contains("YES")){
                String key = cmd.substring(3);
                try {
                    Connection con = connectdb();
                    Statement sr = con.createStatement();
                    sr.executeUpdate("UPDATE pegawai SET StatusPinjaman=0 WHERE idpegawai="+key);
                    Statement sr1 = con.createStatement();
                    ResultSet c = sr1.executeQuery("SELECT StatusPinjaman FROM pegawai WHERE idpegawai="+key);
                    c.next();
                    int d = c.getInt("StatusPinjaman");
                    cekst(d,stat);
                    close.doClick();
                    ControlAction ca = new ControlAction();
                    //button action
                    if(d==0){
                        pinjamButton.addActionListener(ca);
                        pinjamButton.setActionCommand(key);
                        bukaButton.addActionListener(ca);
                        bukaButton.setActionCommand(WARN2);
                    }else{
                        pinjamButton.addActionListener(ca);
                        pinjamButton.setActionCommand(WARN1);
                        bukaButton.addActionListener(ca);
                        bukaButton.setActionCommand("BUKA"+key);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ILocker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else if(cmd.equals(NO)){
                close.doClick();
                popup(cmd);
                //code for opening will be here
            }else if(cmd.equals(LOGOUT)){
                //
                cl.first(cards);
            }else if(cmd.equals(EXIT)){
                card3.remove(pop);
                card3.revalidate();
                card3.repaint();
            }
        }
    }
    
    //method for checking Status Pinjaman of user
    public void cekst(int a, JLabel b){
        if(a==0){
            b.setText("Status Pinjaman : TIDAK AKTIF");
            stImage.setIcon(new ImageIcon(nactIcon));
            Dimension size1 = stat.getPreferredSize();
            stat.setBounds(100, 140, size1.width, size1.height);
            Dimension size2 = stImage.getPreferredSize();
            stImage.setBounds(800, 145, size2.width, size2.height);
        }else{
            b.setText("Status Pinjaman :    AKTIF");
            stImage.setIcon(new ImageIcon(actIcon));
            Dimension size1 = stat.getPreferredSize();
            stat.setBounds(100, 140, size1.width, size1.height);
            Dimension size2 = stImage.getPreferredSize();
            stImage.setBounds(800, 145, size2.width, size2.height);
        }
    }
    
    @Override
    public void run() {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Result result = null;
            BufferedImage image = null;
            
            //qr scanning using webcam
            if (webcam.isOpen()) {
                if ((image = webcam.getImage()) == null) {
                    continue;
                }
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                //getting the result
                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                    //No result...
                }
            }
            //matching the result with the database
            if (result != null) {
                //turn result into string
                String key = result.getText();
                
                //hidden slide button
                JButton hiButton = new JButton();
                hiButton.setVisible(false);
                card1.add(hiButton);
                ControlAction ca = new ControlAction();
                hiButton.addActionListener(ca);
                hiButton.setActionCommand(NEXT);
                //connecting to the database
                try{
                    Connection con = connectdb();
                    Statement stt = con.createStatement();                    
                    ResultSet rs = stt.executeQuery("SELECT namapegawai FROM pegawai WHERE idpegawai="+key);
                    rs.next();
                    String nama = rs.getString("namapegawai");
                    //if the key in qr code exist in database
                    if(!"".equals(nama)){
                        //go to card3(halaman utama)
                        hiButton.doClick();
                        wel.setText("Selamat Datang,\n"+nama);
                        Dimension size = wel.getPreferredSize();
                        wel.setBounds(380, 8, size.width, size.height);
                        Statement st1 = con.createStatement();
                        ResultSet rt = st1.executeQuery("SELECT StatusPinjaman FROM pegawai WHERE idpegawai="+key);
                        rt.next();
                        int pinj = rt.getInt("StatusPinjaman");
                        cekst(pinj,stat);
                        //button action
                        if(pinj==0){
                            pinjamButton.addActionListener(ca);
                            pinjamButton.setActionCommand(key);
                            bukaButton.addActionListener(ca);
                            bukaButton.setActionCommand(WARN2);
                        }else{
                            pinjamButton.addActionListener(ca);
                            pinjamButton.setActionCommand(WARN1);
                            bukaButton.addActionListener(ca);
                            bukaButton.setActionCommand("BUKA"+key);
                        }
                        logoutButton.addActionListener(ca);
                        logoutButton.setActionCommand(LOGOUT);
                        webcam.close();                        
                    }                       
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } while (true);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "My Thread");
        t.setDaemon(true);
        return t;
    }
    
    //initial components
    private void initComponents(){
        //local declaration
        JTextPane title = new JTextPane();
        JTextPane bantuan = new JTextPane();
        JLabel subTitle = new JLabel();
        JLabel hintTitle = new JLabel();
        File file1 = new File("src/hint.png");
        File file2 = new File("src/CompanyLg.png");
        File file3 = new File("src/back.png");
        File file4 = new File("src/appName.png");
        File file5 = new File("src/menuPane.png");
        File file6 = new File("src/about.png");
            
        //insert menu pane style
        BufferedImage menuP = null;
        try{
            menuP = ImageIO.read(file5);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        //insert hint icon
        BufferedImage hintIcon = null;
        try{
            hintIcon = ImageIO.read(file1);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        //back button handling
        BufferedImage backIcon = null;
        try{
            backIcon = ImageIO.read(file3);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        JButton backButton = new JButton(new ImageIcon(backIcon));
        backButton.setBorder(BorderFactory.createEmptyBorder());
        backButton.setContentAreaFilled(false);
        
        //pinjam button handling
        pinjamButton.setBorder(BorderFactory.createEmptyBorder());
        pinjamButton.setContentAreaFilled(false);
        Dimension size9 = pinjamButton.getPreferredSize();
        pinjamButton.setBounds(100, 220, size9.width, size9.height);
        
        //buka button handling
        bukaButton.setBorder(BorderFactory.createEmptyBorder());
        bukaButton.setContentAreaFilled(false);
        bukaButton.setBounds(550, 220, size9.width, size9.height);
        
        //about button styling
        BufferedImage aboutIcon = null;
        try{
            aboutIcon = ImageIO.read(file6);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        JButton aboutButton = new JButton(new ImageIcon(aboutIcon));
        aboutButton.setBorder(BorderFactory.createEmptyBorder());
        aboutButton.setContentAreaFilled(false);
        Dimension size6 = aboutButton.getPreferredSize();
        aboutButton.setBounds(20, 640, size6.width, size6.height);
        
        //logout button handling
        logoutButton.setBorder(BorderFactory.createEmptyBorder());
        logoutButton.setContentAreaFilled(false);
        Dimension size10 = logoutButton.getPreferredSize();
        logoutButton.setBounds(380, 530, size10.width, size10.height);
                       
        //insert logo
        BufferedImage logo = null;
        try{
            logo = ImageIO.read(file2);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        //insert appName logo
        BufferedImage appName = null;
        try{
            appName = ImageIO.read(file4);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        //title text-pane styling
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        StyleConstants.setBold(center, true);
        StyleConstants.setForeground(center, Color.WHITE);
        StyleConstants.setFontFamily(center, "arial");
        StyleConstants.setFontSize(center, 48);
        
        //title styling
        title.setText("SILAHKAN\nSCAN ID CARD ANDA");
        title.setOpaque(false);
        title.setEditable(false);
        StyledDocument doc = title.getStyledDocument();
        doc.setParagraphAttributes(0, 104, center, false);
        Dimension size = title.getPreferredSize();
        title.setBounds(420, 120, size.width, size.height);
        
        //subTitle styling
        subTitle.setText("*Dekatkan ID Card Pada Kamera Disamping");
        subTitle.setFont(new Font("arial", Font.BOLD, 20));
        subTitle.setOpaque(false);
        subTitle.setForeground(Color.WHITE);
        Dimension size1 = subTitle.getPreferredSize();
        subTitle.setBounds(470, 680, size1.width, size1.height);
        
        //welcome with name title styling
        SimpleAttributeSet y = new SimpleAttributeSet();
        StyleConstants.setAlignment(y, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(y, Color.WHITE);
        StyleConstants.setFontFamily(y, "arial");
        StyleConstants.setFontSize(y, 36);

        //welcome with name title
        wel.setOpaque(false);
        wel.setEditable(false);
        StyledDocument dec = wel.getStyledDocument();
        dec.setParagraphAttributes(0, 104, y, false);        
        
        //status label styling
        stat.setFont(new Font("arial", Font.BOLD, 40));
        stat.setOpaque(false);
        stat.setForeground(Color.BLACK);
        
        //hintTitle styling
        hintTitle.setText("PUSAT BANTUAN");
        hintTitle.setFont(new Font("bebas", Font.BOLD, 50));
        hintTitle.setOpaque(false);
        hintTitle.setForeground(Color.WHITE);
        Dimension size7 = hintTitle.getPreferredSize();
        hintTitle.setBounds(300, 20, size7.width, size7.height);
        
        //bantuan styling
        //isi bantuan styling
        SimpleAttributeSet isi = new SimpleAttributeSet();
        StyleConstants.setAlignment(isi, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(isi, Color.BLACK);
        StyleConstants.setFontFamily(isi, "arial");
        StyleConstants.setFontSize(isi, 24);
        //judul bantuan styling
        SimpleAttributeSet judul = new SimpleAttributeSet();
        StyleConstants.setForeground(judul, Color.BLACK);
        StyleConstants.setFontFamily(judul, "arial");
        StyleConstants.setFontSize(judul, 26);
        StyleConstants.setBold(judul, true);
        //insert style
        bantuan.setText("Cara Penggunaan :\n\n"
                + "1. Scan ID Card pegawai anda pada kamera disamping.\n\n"
                + "2. Setelah Masuk Menu Utama, maka akan tertera nama dan status pinjaman anda.\n\n"
                + "3. Tekan tombol PINJAM LOKER jika anda ingin meminjam loker.\n\n"
                + "4. Tekan tombol BUKA LOKER jika anda ingin membuka loker.\n\n"
                + "5. Saat membuka anda bisa memilih untuk mengakiri pinjaman atau tidak.\n\n"
                + "catatan : anda tidak bisa membuka loker jika belum meminjam.");
        bantuan.setOpaque(false);
        bantuan.setEditable(false);
        StyledDocument dc = bantuan.getStyledDocument();
        dc.setParagraphAttributes(18, 1000, isi, false);
        dc.setCharacterAttributes(0, 17, judul, true);
        Dimension size8 = bantuan.getPreferredSize();
        bantuan.setBounds(90, 120, size8.width, size8.height);
        
        //panel styling
        //create webcam object
        Dimension size13 = WebcamResolution.QVGA.getSize();
        webcam = Webcam.getWebcams().get(0); //0 is default webcam
        webcam.setViewSize(size13);
        //create webcam panel
        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size13);
        panel.setFPSDisplayed(true);
        panel.setBounds(510, 320, size13.width, size13.height);
        executor.execute(this);
        
        //card1(halaman awal)
        card1.setLayout(null);
        card1.setBackground(Color.decode("#f9aa75"));
        JLabel lc1=new JLabel(new ImageIcon(logo));
        Dimension size2 = lc1.getPreferredSize();
        lc1.setBounds(20, 20, size2.width, size2.height);
        card1.add(lc1);
        card1.add(title);
        card1.add(subTitle);
        JButton hintButton1 = new JButton(new ImageIcon(hintIcon));
        hintButton1.setBorder(BorderFactory.createEmptyBorder());
        hintButton1.setContentAreaFilled(false);
        Dimension size0 = hintButton1.getPreferredSize();
        hintButton1.setBounds(1230, -1, size0.width, size0.height);
        card1.add(hintButton1);
        card1.add(panel);     
                       
        //card2(halaman hint)
        card2.setLayout(null);
        card2.setBackground(Color.decode("#718fc8"));
        JLabel lc2=new JLabel(new ImageIcon(logo));
        lc2.setBounds(20, 20, size2.width, size2.height);
        card2.add(lc2);
        backButton.setBounds(1230, -1, size0.width, size0.height);
        card2.add(backButton);
        JLabel apn1=new JLabel(new ImageIcon(appName));
        Dimension size4 = apn1.getPreferredSize();
        apn1.setBounds(560, 40, size4.width, size4.height);
        card2.add(apn1);
        JLabel menuPane1 = new JLabel(new ImageIcon(menuP));
        Dimension size5 = menuPane1.getPreferredSize();
        menuPane1.setBounds(170, 130, size5.width, size5.height);
        card2.add(menuPane1);
        card2.add(aboutButton);
        menuPane1.add(hintTitle);
        menuPane1.add(bantuan);
        
        //card3(halaman utama)
        card3.setOpaque(true);
        card3.setLayout(null);
        card3.setBackground(Color.decode("#f9aa75"));
        JLabel lc3=new JLabel(new ImageIcon(logo));
        lc3.setBounds(20, 20, size2.width, size2.height);
        card3.add(lc3,1,0);
        JLabel apn2=new JLabel(new ImageIcon(appName));
        apn2.setBounds(560, 40, size4.width, size4.height);
        card3.add(apn2,2,0);
        JButton hintButton2 = new JButton(new ImageIcon(hintIcon));
        hintButton2.setBorder(BorderFactory.createEmptyBorder());
        hintButton2.setContentAreaFilled(false);
        hintButton2.setBounds(1230, -1, size0.width, size0.height);
        card3.add(hintButton2,4,0);
        JLabel menuPane2 = new JLabel(new ImageIcon(menuP));
        menuPane2.setBounds(170, 130, size5.width, size5.height);
        card3.add(menuPane2,5,0);
        menuPane2.add(wel);
        menuPane2.add(stat);
        menuPane2.add(stImage);
        menuPane2.add(pinjamButton);
        menuPane2.add(bukaButton);
        menuPane2.add(logoutButton);
        
        //layout
        cards.add(card1);
        cards.add(card3);
        cards.add(card2);
        
        //frame styling
        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();
        frame.add(cards);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        device.setFullScreenWindow(frame); 
        
        //action listener for control button
        class ControlActionListenter implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (cards.getLayout());
                String cmd = e.getActionCommand();
                if (cmd.equals(HINT)) {
                    backButton.setActionCommand(FIRST);
                    cl.last(cards);
                }else if (cmd.equals(BACK)) {
                    cl.previous(cards);
                }else if (cmd.equals(HINT1)) {
                    backButton.setActionCommand(BACK);
                    cl.last(cards);
                }else if (cmd.equals(FIRST)) {
                    cl.first(cards);
                }
            }
        }
        
        //inserting action to button
        ControlActionListenter cal = new ControlActionListenter();
        hintButton1.setActionCommand(HINT);
        hintButton1.addActionListener(cal);
        hintButton2.setActionCommand(HINT1);
        hintButton2.addActionListener(cal);
        backButton.addActionListener(cal);
    }   
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ILocker app = null;
            try {
                app = new ILocker();
            } catch (IOException ex) {
                Logger.getLogger(ILocker.class.getName()).log(Level.SEVERE, null, ex);
            }
            app.setVisible(false);
        });
    }
}

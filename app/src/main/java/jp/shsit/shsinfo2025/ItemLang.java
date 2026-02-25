package jp.shsit.shsinfo2025;

public class ItemLang {
   private  CharSequence no;
   private  CharSequence page;
   private  CharSequence Jap;
   private  CharSequence Eng;
   private  CharSequence Small;

   private  CharSequence Vnm;

   private  CharSequence Chn;

   public ItemLang(){
      no ="";
      page = "";
      Jap = "";
      Eng = "";
      Small = "";
      Vnm ="";
      Chn = "";
   }

   public void setNo(CharSequence no) {
      this.no = no;
   }

   public void setPage(CharSequence page) {
      this.page = page;
   }

   public void setJap(CharSequence jap) {
      Jap = jap;
   }

   public void setEng(CharSequence eng) {
      Eng = eng;
   }

   public void setSmall(CharSequence small) {
      Small = small;
   }

   public void setVnm(CharSequence vnm) {Vnm =vnm;}

   public void setChn(CharSequence chn) {Chn =chn;}

   public CharSequence getNo() {
      return no;
   }

   public CharSequence getPage() {
      return page;
   }

   public CharSequence getJap() {
      return Jap;
   }

   public CharSequence getEng() {
      return Eng;
   }

   public CharSequence getSmall() { return Small; }

   public CharSequence getVnm() { return Vnm; }

   public CharSequence getChn() { return Chn; }

}

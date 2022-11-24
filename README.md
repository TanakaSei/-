# まかないセレクター
## Requirement
- realm
- kotlin 1.7.0
- API Level 32
## インストール方法
MakaniSelector.zipをダウンロード，解凍し，androidstudioにて通常の手順に従ってエミュレータ若しくは実機にインストールすることで利用可能となります．
#### 以下動作未検証のインストール方法
release/releas.zip内にあるapp-release.apkをAndroid上にインストールすることで実行可能となります．

## 概要
本アプリケーションはある，居酒屋チェーン店のアルバイトスタッフのまかないを決定するために作成されました． 
また，まかないにはデーザートやドリンクなどの一部のメニューは対象外となっていますが，最後の一品だけが決められないという場合や一般利用者として訪れた際にも利用できるよう抽選対象のカテゴリーや抽選数を変更できるようになっています．

## References
  Realm公式ドキュメント：https://www.mongodb.com/docs/realm/sdk/java/install/#installation

## 今後の展望
ユーザビリティ面での問題が多く，サービスとしてもネイティブアプリだと別プラットフォームへの展開にもコストがかかるため，バックエンドにLaravel9,フロントエンドにVue.jsを採用したWebアプリケーションの製作をしております．
リポジトリ開発中のは[こちら](https://github.com/TanakaSei/makanai_webapp)

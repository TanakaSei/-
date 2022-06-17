# まかないセレクター
ただいま編集中です
## Requirement
- realm
- kotlin 1.7.0
- API Level 32

## 説明（仮）
androidstudioにてプロジェクトを作成後生成されたsrcフォルダに各.ktファイルをコピーし，実行環境のプロジェクトに合ったパッケージ名にpackage your_packagename部分を修正する．
同様にresフォルダに生成されたdrawableフォルダ内にdrawableフォルダ内の画像をすべてコピー．
layoutフォルダ内に同名のフォルダ内の各.xmlファイルをコピー
values内のstringも同様にコピー
プロジェクト，モジュールの各.gradleにMongo公式ドキュメントを参照し，realms関係の依存関係を追記
  公式ドキュメント：https://www.mongodb.com/docs/realm/sdk/java/install/#installation
  
AndroidManifest.xmlに
android:name=".CustomApplication"

<activity android:name=".LotteryResult"/>
<activity android:name=".ShowMenuActivity"/>
<activity android:name=".OptionActivity"/>

をそれぞれ追加


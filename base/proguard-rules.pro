# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging*/
-allowaccessmodification
-repackageclasses ''
-verbose
-keepattributes *Annotation*
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-keepparameternames

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep enum * { *; }
-keep interface * { *; }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends androidx.fragment.app.Fragment
-keep public class androidx.*

-keep public class com.quangph.base.mvp.action.ActionManager {
    public <fields>;
    public <methods>;
}

-keep public class com.quangph.base.mvp.action.ActionManager$* {
    public <fields>;
    public <methods>;
}

-keep public class com.quangph.base.mvp.action.Action {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.action.Action$* {
    public <fields>;
    public <methods>;
}

-keep public class com.quangph.base.mvp.action.uid.IDGenerator {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.action.actionjob.ActionWorker {
    public <fields>;
    public <methods>;
}

-keep public class com.quangph.base.mvp.action.scheduler.AsyncTaskScheduler {
    public <fields>;
    public <methods>;
}

-keep public class com.quangph.base.mvp.action.ActionException {
    public <fields>;
    public <methods>;
}

-keep public class com.quangph.base.mvp.action.ActionStartErrorException {
    public <fields>;
    public <methods>;
}

-keep public class com.quangph.base.mvp.action.CompoundCallback {
    public <fields>;
    public <methods>;
}

-keep class com.quangph.base.mvp.action.ActionExtensionKt {*; }

-keep public class com.quangph.base.viewbinder.Layout {
    public <fields>;
    public <methods>;
}

-keep public class com.quangph.base.viewbinder.ViewBinder {
    public <fields>;
    public <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.MVPActivity {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.MVPFragment {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.BaseChildPresenter {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.BasePresenter {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.MVPState {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.view.BaseCardView {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.view.BaseCoordinatorView {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.view.BaseDrawerLayout {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.view.BaseFrameView {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.view.BaseLinearView {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.view.BaseNestedScrollView {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.view.BaseRelativeView {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.view.BaseScrollView {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.mvpcomponent.view.BaseSwipeRefreshLayout {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.domain.SimpleCallback {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.NotifyViewModel {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.SafeClicked {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.adapter.BaseRclvAdapter {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.adapter.BaseRclvHolder {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.adapter.BaseRclvHolder$*{
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.adapter.BaseVHData {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.adapter.group.GroupData {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.adapter.group.GroupManager {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.adapter.group.GroupRclvVH {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.kt.KRclvAdapter {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.kt.KRclvVHInfo {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.kt.KVH {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.UIJobScheduler {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.recyclerview.dragdrophelper.* {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.drag.FlingHelper {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.drag.MoveGestureDetector {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.drag.MoveGestureDetector$* {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.repo.BaseRepo {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.mvp.repo.BaseRepo$* {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.common.KeyboardTouchManager {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.common.BaseActivity {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.common.BaseFragment {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.common.BaseApplication {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.common.BaseMultiDexApplication {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.common.converter.ListConverter {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.common.converter.Mapper {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.common.converter.Mapper$* {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.common.converter.MapperKt {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.common.converter.ArrayConverter {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.dispatcher.BaseDispatcher {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.cache.CacheAppEvent {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.cache.CacheManager {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.cache.DefaultCacheFactory {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.cache.LruCacheImpl {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.cache.SequenceCache {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.lifecycle.LCDelegate {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.security.Encryptor {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.socket.SimpleSocket {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.socket.SocketMessage {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.statemachine.UIStateMachine {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.statemachine.UIState {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.thread.BaseAsyncTask {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.thread.BaseAsyncTask$* {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.thread.SerialExecutor {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.drag.FlingHelper {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.drag.MoveGestureDetector {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.view.drag.MoveGestureDetector$* {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.delegate.AutoSaveDelegate {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.delegate.AutoSaveListLiveDataDelegate {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.delegate.AutoSaveLiveDataDelegate {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.delegate.AutoSaveMapLiveDataDelegate {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.livedata.UpdatableLiveData {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.livedata.collection.CollectionLiveData {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.livedata.collection.CollectionLiveDataObserver {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.livedata.collection.CollectionLiveDataObserverInfo {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.livedata.collection.ListLiveData {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.livedata.map.HashMapLiveDataObserver {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.livedata.map.MapLiveData {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.livedata.map.MapLiveDataObserverInfo {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.observer.DelayCollectionObserver {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.observer.DelayMapObserver {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.observer.DelayObserver {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.DefaultDelayObserverFactory {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.DelayObserverFactory {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.KAutoSaveViewModel {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.NotifyViewModel {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.SaveStateData {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.viewmodel.SingleEventData {
    !private <fields>;
    !private <methods>;
}


-keep public class com.quangph.pattern.behavior.BehaviorAgent {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.pattern.behavior.RuleFactory {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.pattern.behavior.Condition {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.eventbus.PendingEventBus {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.eventbus.PendingSubscriber {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.eventbus.EventMap {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.eventbus.EventMap$* {
    public <fields>;
    public <methods>;
}

-keep public class com.quangph.jetpack.kotlin.ComparatorElement {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.kotlin.SingletonHolder {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.JetActivity {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.JetFragment {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.JetTabFragment {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.JetApplication {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.JetMultiDexApplication {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.JetTrackerFactory {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.JetActionCallback {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.JetBottomSheetDialog {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.JetException {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.alert.HeaderAlert {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.alert.HeaderAlertDefault {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.autobg.AutoBackgroundDrawable {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.autobg.AutoBgImageView {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.error.NetworkErrorImpl {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.gallery.CameraCaptureSupport {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.gallery.MediaGallery {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.imageloader.ImageLoaderFactory {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.imageloader.glide.BitmapTransformation {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.imageloader.glide.GlideImageLoader {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.imageloader.glide.RoundedCornersTransformation {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.view.* {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.view.recyclerview.wrap.*{
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.view.recyclerview.decor.*{
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.retrofit.BaseRetrofitConfig {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.retrofit.GsonUtcDateAdapter {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.retrofit.NullOnEmptyConverterFactory {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.retrofit.RetroConfigParams {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.retrofit.RetrofitFactory {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.jetpack.print.* {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.base.tracking.Tracker {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.pattern.CORBase {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.pattern.node.* {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.pattern.node.finder.* {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.pattern.node.traverse.* {
    !private <fields>;
    !private <methods>;
}

-keep public class com.quangph.pattern.spec.* {
    !private <fields>;
    !private <methods>;
}
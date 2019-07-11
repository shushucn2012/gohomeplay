package com.park61.teacherhelper.common.set;

/**
 * APP通信地址
 *
 * @author super
 */
public class AppUrl {
    /**
     * app通信访问主地址
     */
    public static String host = "";

    /**
     * app通信访问正式地址
     */
    public static final String releaseHost = "https://api.61park.cn/t";

    /**
     * app通信访问测试地址
     */
    public static final String demoHost = "https://api.61park.cn/t";


    public static final String SHARE_APP_ICON = "http://a2.qpic.cn/psb?/4b8a92ff-3677-40b5-80ba-465cc9ff57bc/kG4mHlxqY7q87xy7Ti5Uwdh21oEO*08JF7DKf49xfO4!/b/dGwBAAAAAAAA&bo=eAB4AAAAAAADACU!&rf=viewer_4";

    /**
     * 请求阿里云oss加密后的url
     */
    public static final String GET_ALIYUN_SEC_URL = "/service/upload/imgUpload";

    /**
     * 获取班级列表接口
     */
    public static final String GET_CLAZZ_LIST = "/service/teachGClass/listMy";
    /**
     * 获取班级类型的字典表
     */
    public static final String GET_CLAZZ_TYPE = "/service/dict/list";
    /**
     * 获取班级老师列表接口
     */
    public static final String GET_CLAZZ_TEACHER_LIST = "/service/teachGClass/listTeacher";
    /**
     * 获取班级宝贝列表接口
     */
    public static final String GET_CLAZZ_CHILD_LIST = "/service/teachGClass/listChild";
    /**
     * 获取宝贝详情接口
     */
    public static final String GET_CLAZZ_CHILD_DETAIL = "/service/teachGClass/childDetail";
    /**
     * 新增班级
     */
    public static final String SAVE_NEW_CLAZZ = "/service/teachGClass/add";
    /**
     * 更新班级
     */
    public static final String UPDATE_CLAZZ_INFO = "/service/teachGClass/update";

    public static final String EXIT_CLAZZ = "/service/teachGClass/exit";

    public static final String teachCourse_item = "/service/content/get/byContentId";

    public static final String teachCourse_intro = "/service/teachCourse/intro";

    public static final String teachCourse_comt = "/service/teachCourseEvaluate/listTeachCourseEvaluate";

    public static final String teachCourse_source = "/service/teachCourse/source";

    public static final String sendLoginVerifyCode = "/service/login/sendLoginVerifyCode";

    public static final String loginByVerifyCode = "/service/login/loginByVerifyCode";

    public static final String teachPromPostion_commend = "/service/teachPromPostion/commend";

    public static final String teachPromPostion_more = "/service/teachPromPostion/more";

    public static final String teachPromPostion_banner = "/service/teachPromPostion/banner";

    public static final String teachMyCourse_add = "/service/content/collect";

    public static final String teachMyCourse_cancel = "/service/content/unCollect ";

    public static final String listTeachMyCourse = "/service/teachMyCourse/listTeachMyCourse";

    public static final String teachMyCourse_stick = "/service/teachMyCourse/stick";

    public static final String addFeedBack = "/service/feedback/addFeedBack";

    public static final String addRecord = "/service/learnRecord/addRecord";

    public static final String addRecordTime = "/service/learnRecord/addRecordTime";

    public static final String findRecodeTime = "/service/learnRecord/findRecodeTime";

    public static final String recordList = "/service/learnRecord/recordList";

    public static final String delRecord = "/service/learnRecord/delRecord";

    public static final String myInfor = "/service/teachMember/myInfor";

    public static final String history = "/service/teachIssuePhoto/history";

    public static final String issuePhoto = "/service/teachIssuePhoto/issuePhoto";

    public static final String noticeList = "/service/teachNotice/noticeList";

    public static final String addNotice = "/service/teachNotice/addNotice";

    public static final String updateMyInfor = "/service/teachMember/updateMyInfor";

    public static final String addUserViewNum = "/service/teachCourse/addUserViewNum";

    public static final String addCourseViewNum = "/service/teachCourse/addCourseViewNum";

    public static final String teachCourseEvaluate_add = "/service/teachCourseEvaluate/add";

    public static final String LOGIN_OUT = "/service/login/logout";

    public static final String addClassTeacher = "/service/teachGClass/addClassTeacher";
    public static final String addTeacherList = "/service/goldTeacher/getContentListByTrainer";
    public static final String addActivityCity = "/service/cms/loadPageDataById";
    public static final String addActivitylist = "/service/cms/loadModuleDataById";
    public static final String addActivityTop = "/service/cms/getTeachDoubleElevenPage";
    /**
     * 联合登陆
     */
    public static final String UNION_LOGIN = "/service/login/unoinLogin";

    /**
     * 联合登录验证手机号是否绑定url后缀
     */
    public static final String UNION_LOGIN_CHECK_MOBILE = "/service/login/unoinLoginCheckMobile";

    /**
     * 登录url后缀
     */
    public static final String LOGIN = "/service/login/login";

    /**
     * 发送注册验证码url后缀
     */
    public static final String SEND_REGISTER_VCODE = "/service/login/sendRegisterVerifyCode";

    public static final String sendUnionLoginBindMobile = "/service/login/sendUnionLoginBindMobile";

    public static final String checkLoginAccountIsExist = "/service/login/checkUserMobile";

    public static final String register = "/service/login/register";

    public static final String checkRegisterVerifyCode = "/service/login/checkRegisterVerifyCode";

    public static final String checkModifyPwdVerifyCode = "/service/login/checkModifyPwdVerifyCode";

    /**
     * 发送重置密码验证码url后缀
     */
    public static final String SEND_RESET_VCODE = "/service/login/sendModifyPwdVerifyCode";

    public static final String checkVerifyCode = "/service/login/checkVerifyCode";

    public static final String modifyPwd = "/service/login/modifyPwd";

    /**
     * 将友盟token上传到服务器
     */
    public static final String SAVE_APP_DEVICE_TOKEN = "/service/appDevice/saveAppDeviceToken";

    public static final String groupList = "/service/data/groupList";

    public static final String addGroup = "/service/data/addGroup";

    public static final String address = "/service/data/address";

    public static final String updateGroup = "/service/data/updateGroup";

    public static final String search_hot = "/service/search/hot";

    public static final String search_history = "/service/search/history";

    public static final String search = "/service/search/content";

    public static final String contentCategory_list = "/service/contentCategory/list";

    public static final String dict_list = "/service/dict/list";

    public static final String getTeachHomePage = "/service/cms/getTeachHomePage";

    public static final String getGoldTeacherList = "/service/goldTeacher/getGoldTeacherList";

    public static final String goldTeacherListNew = "/service/trainer/trainerList";

    public static final String getGuessContentList = "/service/cms/getGuessContentList";

    public static final String search_history_clear = "/service/search/history/clear";

    public static final String addContentCommentSend = "/service/content/addComment";

    public static final String addContentAdmire = "/service/content/zan";

    public static final String addContentRecommend = "/service/content/get/recommend";

    public static final String addContentComment = "/service/content/commentList";

    public static final String addTeachdetail = "/service/goldTeacher/getTrainerDetail";

    //是否关注
    public static final String isFollowById = "/service/focus/findByCollectTrainer";

    //关注
    public static final String followTeacher = "/service/focus/collectTrainer";

    //取消关注
    public static final String unfollowTeacher = "/service/focus/unCollectTrainer";

    public static final String activity_confirm = "/service/activity/confirm";

    public static final String activity_submit = "/service/activity/submit";

    public static final String GET_PAY_INFO = "/service/activity/paymentInfo";

    public static final String SEND_PAYMENT_VERIFYCODE = "/service/activity/sendPaymentVerifyCode";

    public static final String activity_pay = "/service/activity/pay";

    public static final String activityDetail = "/service/activity/activityDetail";

    public static final String addQuestion = "/service/activity/addQuestion";

    public static final String contentBrowseCheck = "/service/activity/contentBrowseCheck";

    public static final String eventDetail = "/service/activity/eventDetail";

    public static final String myActivityApplyList = "/service/activity/myActivityApplyList";

    public static final String activityApplyDetail = "/service/activity/activityApplyDetail";

    public static final String cancelOrder = "/service/activity/cancelOrder";

    public static final String checkUpdateStatus = "/service/app/checkUpdateStatus";

    public static final String christmasContent = "/service/search/christmasContent";

    public static final String praise = "/service/content/praise";

    public static final String getAndroidStartPage = "/service/page/getAndroidStartPage";

    public static final String christmasActivityDetail = "/service/activity/christmasActivityDetail";

    //亲子活动列表
    public static final String myActivity = "/service/party/loadMyPartyList";
    public static final String myActivityLib = "/service/party/partyTemplateList";
    public static final String myClassList = "/service/teachGClass/listMy";
    public static final String publishLib = "/service/party/createParty";
    public static final String myLibDetail = "/service/party/findPartyTemplateDetail";
    public static final String myLibToyList = "/service/party/partyTemplateProductList";

    //亲子活动详情
    public static final String childActivityDetail = "/service/party/partyDetail";

    //亲子活动商品列表
    public static final String childActivityToys = "/service/party/partyMerchandiseList";
    public static final String toyDetail = "/service/pmInfo/detail";

    //亲子活动报名数据列表
    public static final String activitySignData = "/service/party/getPartyApplyList";

    //亲子活动扫码签到
    public static final String signUpForActivity = "/service/party/sign";

    //新增双11活动签到
    public static final String signUpDoubleE = "/service/activity/signIn";

    //音视频详情
    public static final String findContentDetailById = "/service/content/findContentDetailById";

    //音频列表
    public static final String getAudioList = "/service/content/getAudioList";

    //根据资源id获取播放凭证
    public static final String videoPlayAuth = "/service/contentSource/videoPlayAuth";

    //增加播放次数
    public static final String addViewNum = "/service/contentSource/addViewNum";

    //增加下载次数
    public static final String addDownLoadNum = "/service/contentSource/addDownLoadNum";

    //视频列表
    public static final String findContentVideoList = "/service/content/findContentVideoList";

    //视频列表
    public static final String getAttachmentList = "/service/content/getAttachmentList";

    //权益查看
    public static final String selectMyService = "/service/rights/getServiceByBatchCode";

    //我的服务列表
    public static final String getMyServiceList = "/service/rights/getMyServiceList";

    //服务申请
    public static final String applyMyService = "/service/rights/serviceApply";

    //服务申请详情
    public static final String getMyServiceDetail = "/service/rights/getMyServiceDetail";

    //挂失申请文描
    public static final String serviceStatusDetail = "/service/rights/serviceStatusDetail";

    //挂失申请提交
    public static final String addLostRecord = "/service/rights/addLostRecord";

    //确认服务完成
    public static final String changeStatusToFinish = "/service/rights/changeStatusToFinish";

    //我关注的专家列表
    public static final String myFocusTrainer = "/service/focus/myFocusTrainer";

    //系列课程列表首页详情
    public static final String getCourseDetailById = "/service/TrainerCourseSeries/findCourseSeriesByID";
    //课程课时列表
    public static final String getCourseListById = "/service/TrainerCourse/findCourseList";
    //课时详情 nana
    public static final String getSubCourseDetail = "/service/TrainerCourseSeries/trainerCourseDetail";
    //课时学习次数 nana
    public static final String addSubCourseViewNum = "/service/TrainerCourseSeries/saveStudyTrainerCourse";
    //推荐课时，上一个和下一个
    public static final String getRecommendCourse = "/service/TrainerCourseSeries/moreTrainerCourse";
    //保存课时学习记录
    public static final String saveStudyRecord = "/service/TrainerCourseSeries/saveStudyTrainerCourse";
    //确认订单
    public static final String getCourseOrderInfo = "/service/TrainerCourseSeries/confirm";
    //提交订单
    public static final String submitCourseOrder = "/service/TrainerCourseSeries/submit";
    //查询优惠券
    public static final String getCanUseCoupon = "/service/coupon/getCourseCanUseCoupon";

    //我收藏的内容列表
    public static final String myFocusContent = "/service/focus/myFocusContent";

    //我优惠券列表
    public static final String getUserCouponList = "/service/coupon/getUserCouponList";

    //我的已购买的课程
    public static final String myCourseBuyList = "/service/TrainerCourseSeries/myCourseBuyList";

    //我最近学习的课程
    public static final String myCourseList = "/service/TrainerCourseSeries/myCourseList";

    //我的订单列表
    public static final String findMyCourseSoList = "/service/TrainerCourse/findMyCourseSoList";

    //我的订单详情
    public static final String courseSoDetail = "/service/TrainerCourse/courseSoDetail";

    //我的培训列表
    public static final String listMyTraining = "/service/training/listMyTraining";
    //知识库列表
    public static final String myKnowledgeList = "/service/taskCalendar/getKnowledgeLibraryList";
    //当月打点数据
    public static final String monthTaskPoint = "/service/taskCalendar/getCalendarList";
    //日期查询，当天
    public static final String findTaskByDate = "/service/taskCalendar/getTaskListByStartDate";

    //行事历列表
    public static final String getTaskListByDateType = "/service/taskCalendar/getTaskListByDateType";

    public static final String getUserTaskListByStatus = "/service/taskCalendar/getUserTaskListByStatus";
    //完成/重启任务接口
    public static final String restartTask = "/service/taskCalendar/restartTask";
    //共享任务老师列表
    public static final String shareTeacherList = "/service/taskCalendar/shareTeacherTaskList";
    //任务共享
    public static final String shareCalendarTask = "/service/taskCalendar/taskShare";

    //获取任务数量
    public static final String getTaskCount = "/service/taskCalendar/getTaskCount";

    //获取服务器时间（时间戳）
    public static final String getServerTimeStamp = "/service/taskCalendar/getServerTimeStamp";

    //任务逾期列表
    public static final String findOverdueList = "/service/taskCalendar/findOverdueList";

    //任务留言
    public static final String addMessage = "/service/taskCalendar/addMessage";

    //获取任务信息
    public static final String getTaskDetailById = "/service/taskCalendar/getTaskDetailById";

    //任务调整
    public static final String adjustTask = "/service/taskCalendar/adjustTask";

    //任务动态列表
    public static final String getTaskLogList = "/service/taskCalendar/getTaskLogList";

    //批量完成yu期任务
    public static final String batchFinishDueTask = "/service/taskCalendar/batchFinishDueTask";

    //添加幼儿园老师职务列表
    public static final String getMemberDuties = "/service/teachMember/getMemberDuties";

    //完善信息默认地址
    public static final String getDefaultAddress = "/service/data/getDefaultAddressByPosition";

    //获取完善信息职务列表
    public static final String getDutyList = "/service/teachMember/getUserDuty";

    //提交园长信息
    public static final String submitTeach = "/service/teachMember/commitTeach";

    //提交代理商信息
    public static final String submitAgent = "/service/teachMember/commitAgent";

    //完善信息后领取优惠券 parkServer
//    public static final String receiveCoupon = "http://10.10.10.14:8380/service/coupon/receiveCoupon";
    public static final String receiveCoupon = "http://api.61park.cn/park/service/coupon/receiveCoupon";

    //优惠券领取界面查看
    public static final String lookCoupon = "/service/coupon/getReceivedCouponList";

    //新建幼儿园
    public static final String createGroup = "/service/data/createGroup";

    //首页最新推荐分页
    public static final String getNewRecommendContent = "/service/content/getNewRecommendContent";

    //专家讲堂列表
    public static final String trainerCourseList = "/service/TrainerCourseSeries/trainerCourseList";

    //基地园介绍
    public static final String introParkApply = "/service/gardenApply/findApplyIntroductionUrl";
    //基地园信息图标列表
    public static final String infoIconList = "/service/gardenClassify/classifyList";
    //基地园申请列表
    public static final String parkApplyList = "/service/gardenApply/getApplyList";
    //基地园申请列表
    public static final String findNoteUrl = "/service/gardenApply/findApplyNoteUrl";
    //提交基础信息
    public static final String submitParkBase = "/service/gardenApply/addApplyInfo";
    //获取基础信息
    public static final String getParkBase = "/service/gardenApply/getApplyInfo";
    //提交图片分类项
    public static final String addApplyItem = "/service/gardenApply/addApplyItem";
    //获取非基础信息
    public static final String getGardenItem = "/service/gardenApply/getGardenItem";
    //获取视频占位信息
    public static final String getAliyunAuth = "/service/video/getAliyunAuth";
    //提交视频分类项
    public static final String addApplyItemVideo = "/service/gardenApply/addApplyItemVideo";
    //提交申请
    public static final String submitApply = "/service/gardenApply/submitApply";
    //获取申请详情
    public static final String getApplyDetail = "/service/gardenApply/getApplyDetail";
    //重新提交改变状态
    public static final String resubmitApply = "/service/gardenApply/resubmitApply";
    //获取STS安全凭证
    public static final String getSTSToken = "/service/video/getSTSToken";
    //获取未查看消息数量
    public static final String messageNum = "/service/messagePush/messageNum";
    //获取消息推送列表
    public static final String messageList = "/service/messagePush/messageList";
    //当前用户消息状态批量更新
    public static final String updateViewStatus = "/service/messagePush/updateViewStatus";
    //消息查看
    public static final String findByMessageId = "/service/messagePush/findByMessageId";
    //音频是否可导出
    public static final String isAudioCanExport = "/service/content/isAudioCanExport";
    //课程体系可领优惠券列表
    public static final String courseReceiveCouponList = "/service/coupon/courseReceiveCouponList";
    //领取单个优惠券
    public static final String receiveSingleCoupon = "/service/coupon/receiveSingleCoupon";
    //当月管理员打点数据
    public static final String getAdminCalendarList = "/service/taskCalendar/getAdminCalendarList";
    //幼儿园管理员行事历日期任务列表
    public static final String getAdminTaskListByStartDate = "/service/taskCalendar/getAdminTaskListByStartDate";
    //用户是否幼儿园管理员
    public static final String userIsKindergartenAdmin = "/service/teachMember/userIsKindergartenAdmin";
    //添加任务
    public static final String addTask = "/service/taskCalendar/addTask";
    //一级分类
    public static final String level1List = "/service/taskCalendar/level1List";
    //获取执行人列表
    public static final String TeachGroupTaskList = "/service/taskCalendar/TeachGroupTaskList";
    //获取任务信息
    //public static final String getTaskDetailById = "/service/taskCalendar/getTaskDetailById";
    //删除行事历任务
    public static final String deleteTaskCalendar = "/service/taskCalendar/deleteTaskCalendar";
    //已分配模板列表
    public static final String assignedTaskCalendarList = "/service/taskCalendar/assignedTaskCalendarList";
    //通知督办
    public static final String notifyExecutor = "/service/taskCalendar/notifyExecutor";
    //个人行事历首页
    public static final String getStatisticalCalendarList = "/service/taskCalendar/getStatisticalCalendarList";
    //获取模版列表
    public static final String templateList = "/service/taskCalendar/templateList";
    //获取模版任务列表
    public static final String templateTaskList = "/service/taskCalendar/templateTaskList";
    //获取幼儿园班级列表
    public static final String getAssignTaskClassList = "/service/taskCalendar/getAssignTaskClassList";
    //模板分配任务
    public static final String assignTaskCalendar = "/service/taskCalendar/assignTaskCalendar";
    //模板分配任务new
    public static final String assignClassTaskCalendar = "/service/taskCalendar/assignClassTaskCalendar";
    //是否是园中校用户
    public static final String userIsOperationAdmin = "/service/teachMember/userIsOperationAdmin";
    //课程列表
    public static final String getTrainerCourseList = "/service/taskCalendar/getTrainerCourseList";

    public static final String getUserAllTaskCount = "/service/taskCalendar/getUserAllTaskCount";
    //添加学习记录
    public static final String studyTrainerCourse = "/service/taskCalendar/studyTrainerCourse";
    //个人任务完成
    public static final String finishTaskCalendarShare = "/service/taskCalendar/finishTaskCalendarShare";
    //我的兴趣班活动列表
    public static final String myInterestClassActivityList = "/service/interestClass/myInterestClassActivityList";
    //兴趣班活动详情
    public static final String interestClassDetail = "/service/interestClass/interestClassDetail";
    //报名数据
    public static final String interestClassApplyData = "/service/interestClass/applyData";
    //报名数据详情
    public static final String interestClassApplyDataDetail = "/service/interestClass/applyDataDetail";
    //课程详情附件列表
    public static final String trainerCourseAttachmentList = "/service/TrainerCourse/trainerCourseAttachmentList";
    //课程详情附件列表-增加附件下载次数
    public static final String addCourseDownLoadNum = "/service/TrainerCourse/addCourseDownLoadNum";
    //集训活动详情
    public static final String intensifiedTrainingActivityDetail = "/service/intensifiedTraining/intensifiedTrainingActivityDetail";
    //集训活动确认订单
    public static final String intensifiedTraining_confirm = "/service/intensifiedTraining/confirm";
    //订单可用不可用优惠券
    public static final String getOrderCanUseCoupon = "/service/coupon/getOrderCanUseCoupon";
    //集训活动提交订单
    public static final String intensifiedTraining_submit = "/service/intensifiedTraining/submit";
    //选择幼儿园接口优化
    public static final String getTeachGroupList = "/service/data/getTeachGroupList";
    //通过定位信息获取城市id
    public static final String getCityIdByPosition = "/service/data/getCityIdByPosition";
    //通过定位信息获取默认地址信息
    public static final String getDefaultAddressByPosition = "/service/data/getDefaultAddressByPosition";
    //行事历已分配模板列表删除
    public static final String delTaskCalendarTemplateClass = "/service/taskCalendar/delTaskCalendarTemplateClass";
    //行事历任务分配后短信提醒
    public static final String sendTaskCalendarMessage = "/service/taskCalendar/sendTaskCalendarMessage";
    //通过页面id加载服务宝典页面
    public static final String loadPageDataById = "/service/cms/loadPageDataById";
    //通过二级类目查询一级类目下所有的二级类目
    public static final String level2list = "/service/contentCategory/level2list";
    //内容搜索列表
    public static final String searchContentList = "/service/content/searchContentList";
    //评语列表
    public static final String teachCommentList = "/service/teachComment/teachCommentList";
    //班级列表
    public static final String userClassList = "/service/teachComment/userClassList";
    //发布评语
    public static final String issueComment = "/service/teachComment/issueComment";
    //班级宝宝列表
    public static final String listChild = "/service/teachGClass/listChild";
    //添加宝宝
    public static final String addChild = "/service/teachGClass/addChild";
    //删除评语
    public static final String deleteTeachComment = "/service/teachComment/deleteTeachComment";
    //评语详情
    public static final String teachCommentDetail = "/service/teachComment/teachCommentDetail";
    //评语模板列表
    public static final String teachCommentTemplateList = "/service/teachComment/teachCommentTemplateList";
    //保存用户评语模板
    public static final String saveUserCommentTemplate = "/service/teachComment/saveUserCommentTemplate";
    //获取小程序二维码生成信息
    public static final String shareMiniProgramCreateQRCodeInfo = "/service/teachGClass/shareMiniProgramCreateQRCodeInfo";
    //解绑宝宝与班级的关系
    public static final String delClassChild = "/service/teachGClass/delClassChild";
    //删除关注人
    public static final String delChildKinshipRelation = "/service/teachGClass/delChildKinshipRelation";
    //行事历-今日事项
    public static final String getTodayTaskCalendarList = "/service/subscribeTaskCalendar/getTodayTaskCalendarList";
    //行事历-统计当前用户的任务数(专业版)
    public static final String countTaskCalendarNum = "/service/subscribeTaskCalendar/countTaskCalendarNum";
    //行事历-我的行事历列表（订阅版）
    public static final String findMySubscribeTaskCalendarList = "/service/subscribeTaskCalendar/findMySubscribeTaskCalendarList";
    //行事历-全部行事历模板
    public static final String getAllTaskCalendarTemplate = "/service/subscribeTaskCalendar/getAllTaskCalendarTemplate";
    //行事历-用户是否已订阅行事历模板
    public static final String isSubscribeCalendar = "/service/subscribeTaskCalendar/isSubscribeCalendar";
    //行事历-行事历模板详情
    public static final String subscribeCalendarTemplateDetail = "/service/subscribeTaskCalendar/subscribeCalendarTemplateDetail";
    //行事历-订阅行事历
    public static final String subscribeCalendar = "/service/subscribeTaskCalendar/subscribeCalendar";
    //行事历-模板任务列表
    public static final String subscribeTaskCalendarList = "/service/subscribeTaskCalendar/subscribeTaskCalendarList";
    //行事历-完成任务
    public static final String finishSubscribeTask = "/service/subscribeTaskCalendar/finishSubscribeTask";
    //行事历-获取后台字典内容
    public static final String getValueByType = "/service/dict/getValueByType";
    //行事历-申请行事历
    public static final String applyTaskCalendar = "/service/subscribeTaskCalendar/applyTaskCalendar";
    //会员制-团队成员列表
    public static final String teachGroupMemberList = "/service/member/teachGroupMemberList";
    //会员制-待审核成员列表
    public static final String memberListInvite = "/service/member/memberListInvite";
    //会员制-会员申请审核通过
    public static final String acceptMemberApply = "/service/member/acceptMemberApply";
    //会员制-会员申请审核拒绝
    public static final String rejectMemberApply = "/service/member/rejectMemberApply";
    //会员制-会员权益文案
    public static final String memberRightIntro = "/service/member/memberRightIntro";
    //会员制-会员类型列表
    public static final String memberTypeList = "/service/member/memberTypeList";
    //会员制-查询园所会员认证状态
    public static final String memberGroupAuthenticateStatus = "/service/member/memberGroupAuthenticateStatus";
    //会员制-查询用户所在幼儿园信息
    public static final String findTeachMemberByUserId = "/service/teachMember/findTeachMemberByUserId";
    //会员制-园所会员认证提交
    public static final String memberGroupAuthenticate = "/service/member/memberGroupAuthenticate";
    //线上提报-提报记录
    public static final String myIntensifiedTraining = "/service/intensifiedTraining/myIntensifiedTraining";
    //线上提报-提报信息提交
    public static final String saveIntensifiedTraining = "/service/intensifiedTraining/saveIntensifiedTraining";
    //线上提报-提报详情
    public static final String findByIntensifiedTrainingId = "/service/intensifiedTraining/findByIntensifiedTrainingId";
    //线上提报-我的培训
    public static final String myTrainingRecord = "/service/intensifiedTraining/myTrainingRecord";
    //线上提报-培训问卷
    public static final String listQuestion = "/service/training/listQuestion";
    //线上提报-提交反馈问卷
    public static final String submitQuestion = "/service/training/submitQuestion";
    //线上提报-获取用户在培训服务中评价反馈顺位
    public static final String trainingEvaluateCisPosition = "/service/training/trainingEvaluateCisPosition";
    //删除园所会员
    public static final String deleteTeachGroupMember = "/service/member/deleteTeachGroupMember";
    //行事历-我的行事历列表（订阅版）
    public static final String findMySubscribeTaskCalendarListV2 = "/service/subscribeTaskCalendar/findMySubscribeTaskCalendarListV2";
    //app扫描二维码授权登录
    public static final String scanQRCodeLogin = "/service/teachPC/scanQRCodeLogin";
    //判断内容是否有更新
    public static final String taskCalendarIsUpdate = "/service/subscribeTaskCalendar/taskCalendarIsUpdate";
    //全量更新个人下的所有任务
    public static final String updateTaskCalendar = "/service/subscribeTaskCalendar/updateTaskCalendar";




}

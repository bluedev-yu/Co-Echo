package bluedev_yu.coecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import bluedev_yu.coecho.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {

    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firestore = FirebaseFirestore.getInstance() //객체 할당
        firestorage = FirebaseStorage.getInstance()

    }

    //피드 업로드 fun
    fun contentUpload()
    {
        //uid, userid는 임시로 생성
        //explain, imageurl, uid, userid, timestamp, favoriteCount=0, favorites, hashTag

        var contentDTO = ContentDTO()

        //줄글
        contentDTO.explain = "hi"
        //이미지 url
        contentDTO.imageurl = null
        //uid
        contentDTO.uid = "1"
        //userid(닉네임)
        contentDTO.userId = "임시"
        //timestamp
        contentDTO.timestamp = System.currentTimeMillis()

        //Content.user.contentid
        firestore?.collection("Content")?.document(contentDTO.uid!!)?.set(contentDTO) //db에 넣기
    }

    //피드 가져오기 fun
    //fun getContent(collectionname : String, documentname : String) : ContentDTO){


    fun commentUpload(Content : ContentDTO){ //댓글 업로드 fun, 인자는 댓글 쓸 피드

        var comment = ContentDTO.Comment()

        comment.uid = "1" //작성자 uid
        comment.userId = "임시" //작성자 userid
        comment.timestamp = System.currentTimeMillis()
        comment.comment = "댓글 내용"

        //Comment.contentid.Commentid
        firestore?.collection("Comment")?.document(Content.contentId!!)?.set(comment)
    }


}


// Generated by view binder compiler. Do not edit!
package com.example.myapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.myapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityArticleDetailsBinding implements ViewBinding {
  @NonNull
  private final ScrollView rootView;

  @NonNull
  public final TextView articleAuthor;

  @NonNull
  public final TextView articleContent;

  @NonNull
  public final ImageView articleImage;

  @NonNull
  public final TextView articleTitle;

  @NonNull
  public final Button btnBack;

  private ActivityArticleDetailsBinding(@NonNull ScrollView rootView,
      @NonNull TextView articleAuthor, @NonNull TextView articleContent,
      @NonNull ImageView articleImage, @NonNull TextView articleTitle, @NonNull Button btnBack) {
    this.rootView = rootView;
    this.articleAuthor = articleAuthor;
    this.articleContent = articleContent;
    this.articleImage = articleImage;
    this.articleTitle = articleTitle;
    this.btnBack = btnBack;
  }

  @Override
  @NonNull
  public ScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityArticleDetailsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityArticleDetailsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_article_details, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityArticleDetailsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.article_author;
      TextView articleAuthor = ViewBindings.findChildViewById(rootView, id);
      if (articleAuthor == null) {
        break missingId;
      }

      id = R.id.article_content;
      TextView articleContent = ViewBindings.findChildViewById(rootView, id);
      if (articleContent == null) {
        break missingId;
      }

      id = R.id.article_image;
      ImageView articleImage = ViewBindings.findChildViewById(rootView, id);
      if (articleImage == null) {
        break missingId;
      }

      id = R.id.article_title;
      TextView articleTitle = ViewBindings.findChildViewById(rootView, id);
      if (articleTitle == null) {
        break missingId;
      }

      id = R.id.btn_back;
      Button btnBack = ViewBindings.findChildViewById(rootView, id);
      if (btnBack == null) {
        break missingId;
      }

      return new ActivityArticleDetailsBinding((ScrollView) rootView, articleAuthor, articleContent,
          articleImage, articleTitle, btnBack);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}

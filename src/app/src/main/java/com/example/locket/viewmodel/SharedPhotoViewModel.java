package com.example.locket.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.locket.data.PhotoRepository;
import com.example.locket.data.SharedPhotoRepository;
import com.example.locket.model.Photo;
import com.example.locket.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SharedPhotoViewModel extends ViewModel {
    private final SharedPhotoRepository repository;
    private final PhotoRepository photoRepo;
    private final MutableLiveData<List<Photo>> sharedPhotos = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SharedPhotoViewModel() {
        this.repository = new SharedPhotoRepository();
        this.photoRepo = new PhotoRepository();
    }

    public LiveData<List<Photo>> getSharedPhotos(String userId) {
        repository.getSharedPhotos(userId, new PhotoRepository.FirestoreCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> photoIds) {
                if (photoIds == null || photoIds.isEmpty()) {
                    sharedPhotos.setValue(Collections.emptyList());
                } else {
                    photoRepo.getPhotosByIds(photoIds, new PhotoRepository.FirestoreCallback<List<Photo>>() {
                        @Override
                        public void onSuccess(List<Photo> photos) {
                            photos.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
                            sharedPhotos.setValue(photos);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            errorMessage.setValue("Không thể tải danh sách ảnh: " + e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Không thể lấy danh sách ảnh được chia sẻ: " + e.getMessage());
            }
        });

        return sharedPhotos;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void sharePhoto(String photoId, String senderId, List<User> allFriends, List<String> selectedFriendIds) {
        List<String> receivers = selectedFriendIds != null && !selectedFriendIds.isEmpty()
                ? selectedFriendIds
                : new ArrayList<>();

        if (receivers.isEmpty()) {
            for (User friend : allFriends) {
                if (!friend.getUid().equals(senderId)) {
                    receivers.add(friend.getUid());
                }
            }
        }

        for (String receiverId : receivers) {
            repository.sharePhotoToUser(photoId, senderId, receiverId);
        }
    }

    public LiveData<List<Photo>> getPhotosSharedWithMe(String friendId, String myId) {
        repository.getPhotosSharedByFriend(friendId, myId, new PhotoRepository.FirestoreCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> photoIds) {
                if (photoIds == null || photoIds.isEmpty()) {
                    sharedPhotos.setValue(Collections.emptyList());
                } else {
                    photoRepo.getPhotosByIds(photoIds, new PhotoRepository.FirestoreCallback<List<Photo>>() {
                        @Override
                        public void onSuccess(List<Photo> photos) {
                            photos.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
                            sharedPhotos.setValue(photos);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            errorMessage.setValue("Không thể tải ảnh từ bạn bè: " + e.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Lỗi khi tải ảnh từ bạn bè: " + e.getMessage());
            }
        });
        return sharedPhotos;
    }
    public LiveData<List<Photo>> getMyPhotos(String userId) {
        photoRepo.getPhotosByUser(userId, new PhotoRepository.FirestoreCallback<List<Photo>>() {
            @Override
            public void onSuccess(List<Photo> photos) {
                if (photos == null || photos.isEmpty()) {
                    sharedPhotos.setValue(Collections.emptyList());
                } else {
                    sharedPhotos.setValue(photos); // Đã sắp xếp trong getPhotosByUser
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Lỗi khi tải ảnh của bạn: " + e.getMessage());
                sharedPhotos.setValue(Collections.emptyList());
            }
        });
        return sharedPhotos;
    }


}

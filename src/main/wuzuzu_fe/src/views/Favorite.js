// BookmarkPage.js

import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@mui/material';
import sampleImage from '../assets/img/spotDetail.jpg';

const BookmarkPage = () => {
  const [bookmarks, setBookmarks] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(`/api/v1/favorites`, {
          headers: {
            'Authorization': localStorage.getItem('Authorization'),
          },
        });

        if (response.ok) {
          const responseData = await response.json();
          if (responseData.hasOwnProperty('data') && Array.isArray(responseData.data)) {
            setBookmarks(responseData.data);
          } else {
            console.error('Data is not in the expected format');
          }
        } else {
          console.error('Failed to fetch data');
        }
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);


  const handleDeleteBookmark = async (id) => {
    try {
      const requestOptions = {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('Authorization'),
        },
      };
      const response = await fetch(`/api/v1/favorites/${id}`, requestOptions);
      if (response.ok) {
        // 삭제한 항목을 제외한 나머지 항목들로 state를 업데이트합니다.
        const updatedBookmarks = bookmarks.filter(bookmark => bookmark.favoriteId !== id);
        setBookmarks(updatedBookmarks);
      } else {
        console.error('Failed to delete bookmark');
      }
    } catch (error) {
      console.error('Error deleting bookmark:', error);
    }
  };

  return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <div style={{
          backgroundColor: 'white',
          border: '1px solid #ccc',
          borderRadius: '10px',
          padding: '20px',
          marginBottom: '400px',
          width: '40%',
        }}>
          <div style={{
            backgroundColor: '#FBEFEF',
            padding: '10px',
            borderRadius: '10px',
            position: 'relative',
            textAlign: 'center'
          }}>
            <h1 style={{ textAlign: 'center' }}>즐겨찾기 목록</h1>
            {bookmarks.length > 0 ? (
                <ul style={{ listStyleType: 'none', padding: 0 }}>
                  {bookmarks.map((bookmark, index) => (
                      <li key={index} style={{ marginBottom: '10px', display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                        <div>
                          <p>{bookmark.spotName} /  {bookmark.address} / {bookmark.category}</p>
                        </div>
                        <Button
                            variant="contained"
                            onClick={() => handleDeleteBookmark(bookmark.favoriteId)}
                            style={{ backgroundColor: '#FBEFEF', color: '#6E6E6E', width: '150px', height: '50px' }}
                        >
                          삭제
                        </Button>
                      </li>
                  ))}
                </ul>
            ) : (
                <p>즐겨찾기 목록이 없습니다.</p>
            )}
          </div>
          <div style={{
            marginTop: '20px',
            textAlign: 'center',
          }}>
            <Button
                variant="contained"
                onClick={() => navigate(-1)}
                style={{ backgroundColor: '#FBEFEF', color: '#6E6E6E', width: '150px', height: '50px' }}
            >
              돌아가기
            </Button>
          </div>
        </div>
      </div>
  );
};

export default BookmarkPage;

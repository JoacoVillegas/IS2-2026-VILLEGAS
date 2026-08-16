import { useState } from 'react'

// --- 1. Datos "hardcodeados" (simulan lo que vendría de una base de datos) ---
const commentsData = [
  {
    id: 1,
    userName: 'martu_codes',
    avatar: 'https://i.pravatar.cc/48?img=12',
    text: '¡Buenísimo el video!',
    initialLikes: 24
  },
  {
    id: 2,
    userName: 'JuanMaster',
    avatar: 'https://i.pravatar.cc/48?img=33',
    text: 'Like si lo ves en 2026 y te sigue sirviendo',
    initialLikes: 8
  },
  {
    id: 3,
    userName: 'facux90',
    avatar: 'https://i.pravatar.cc/48?img=5',
    text: 'Es demasiado esto de React',
    initialLikes: 3
  },
  {
    id: 4,
    userName: 'soleciitaa_',
    avatar: 'https://i.pravatar.cc/48?img=47',
    text: 'Estuve trabado 2 días con esto y en 10 minutos lo entendí. Gracias totales.',
    initialLikes: 56
  }
]

// --- 2. Componente hijo: UN comentario individual ---
// Recibe props del padre y maneja SU PROPIO estado de like
function Comment({ userName, avatar, text, initialLikes }) {
  // Estado propio de este comentario: si el usuario le dio like o no
  const [liked, setLiked] = useState(false)
  // Estado propio: cuántos likes tiene (arranca en initialLikes)
  const [likes, setLikes] = useState(initialLikes)

  const handleLikeClick = () => {
    if (liked) {
      setLikes(likes - 1)
    } else {
      setLikes(likes + 1)
    }
    setLiked(!liked)
  }

  return (
    <div className="flex gap-3 py-4 border-b border-gray-200 last:border-b-0">
      <img
        src={avatar}
        alt={userName}
        className="w-10 h-10 rounded-full flex-shrink-0"
      />
      <div className="flex-1">
        <p className="text-sm font-semibold text-gray-900">@{userName}</p>
        <p className="text-sm text-gray-700 mt-1">{text}</p>

        <button
          onClick={handleLikeClick}
          className="flex items-center gap-1.5 mt-2 group"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            viewBox="0 0 24 24"
            fill={liked ? '#2563eb' : 'none'}
            stroke={liked ? '#2563eb' : '#6b7280'}
            strokeWidth="2"
            className="w-5 h-5 transition-colors group-hover:stroke-blue-600"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M6.633 10.5c.806 0 1.533-.446 2.031-1.08a9.041 9.041 0 0 1 2.861-2.4c.723-.384 1.35-.956 1.653-1.715a4.498 4.498 0 0 0 .322-1.672V3a.75.75 0 0 1 .75-.75A2.25 2.25 0 0 1 16.5 4.5c0 1.152-.26 2.243-.723 3.218-.266.558.107 1.282.725 1.282h3.126c1.026 0 1.945.694 2.054 1.715.045.422.068.85.068 1.285a11.95 11.95 0 0 1-2.649 7.521c-.388.482-.987.729-1.605.729H13.48c-.483 0-.964-.078-1.423-.23l-3.114-1.04a4.501 4.501 0 0 0-1.423-.23H5.904M14.25 9h2.25M5.904 18.75c.083.205.173.405.27.602.197.4-.078.898-.523.898h-.908c-.889 0-1.713-.518-1.972-1.368a12 12 0 0 1-.521-3.507c0-1.553.295-3.036.831-4.398C3.387 10.203 4.167 9.75 5 9.75h1.053c.472 0 .745.556.5.96a8.958 8.958 0 0 0-1.302 4.665c0 1.194.232 2.333.654 3.375Z"
            />
          </svg>
          <span
            className={`text-xs font-medium ${
              liked ? 'text-blue-600' : 'text-gray-500'
            }`}
          >
            {likes}
          </span>
        </button>
      </div>
    </div>
  )
}

// --- 3. Componente padre: la lista completa ---
export default function CommentSection() {
  return (
    <div className="max-w-md mx-auto bg-white rounded-xl shadow-sm p-5 my-8">
      <h2 className="text-base font-bold text-gray-900 mb-1">
        {commentsData.length} Comentarios
      </h2>
      <div>
        {commentsData.map((comment) => (
          // "key" es obligatorio cuando usás .map(): le sirve a React
          // para identificar cada elemento de la lista sin confundirlos
          <Comment
            key={comment.id}
            userName={comment.userName}
            avatar={comment.avatar}
            text={comment.text}
            initialLikes={comment.initialLikes}
          />
        ))}
      </div>
    </div>
  )
}

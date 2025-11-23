"use client"

import type React from "react"
import { useRef, useState } from "react"
import { useGSAP } from "@gsap/react"
import gsap from "gsap"

interface BlobCursorProps {
  blobType?: "circle" | "square"
  fillColor?: string
  trailCount?: number
  sizes?: number[]
  innerSizes?: number[]
  innerColor?: string
  opacities?: number[]
  shadowColor?: string
  shadowBlur?: number
  shadowOffsetX?: number
  shadowOffsetY?: number
  filterStdDeviation?: number
  useFilter?: boolean
  fastDuration?: number
  slowDuration?: number
  zIndex?: number
}

const BlobCursor: React.FC<BlobCursorProps> = ({
  blobType = "circle",
  fillColor = "#5227FF",
  trailCount = 3,
  sizes = [60, 125, 75],
  innerSizes = [20, 35, 25],
  innerColor = "rgba(255,255,255,0.8)",
  opacities = [0.6, 0.6, 0.6],
  shadowColor = "rgba(0,0,0,0.75)",
  shadowBlur = 5,
  shadowOffsetX = 10,
  shadowOffsetY = 10,
  filterStdDeviation = 30,
  useFilter = true,
  fastDuration = 0.1,
  slowDuration = 0.5,
  zIndex = 100,
}) => {
  const cursorRef = useRef<HTMLDivElement>(null)
  const blobsRef = useRef<(HTMLDivElement | null)[]>([])
  const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 })

  useGSAP(
    () => {
      const moveCursor = (e: MouseEvent) => {
        setMousePosition({ x: e.clientX, y: e.clientY })

        blobsRef.current.forEach((blob, index) => {
          if (blob) {
            const duration = index === 0 ? fastDuration : slowDuration + index * 0.1
            gsap.to(blob, {
              x: e.clientX,
              y: e.clientY,
              duration: duration,
              ease: "power2.out",
            })
          }
        })
      }

      window.addEventListener("mousemove", moveCursor)
      return () => window.removeEventListener("mousemove", moveCursor)
    },
    { scope: cursorRef, dependencies: [fastDuration, slowDuration] },
  )

  return (
    <div ref={cursorRef} className="pointer-events-none fixed inset-0 w-full h-full overflow-hidden" style={{ zIndex }}>
      {useFilter && (
        <svg className="hidden">
          <filter id="blob-filter">
            <feGaussianBlur in="SourceGraphic" stdDeviation={filterStdDeviation} result="blur" />
            <feColorMatrix in="blur" mode="matrix" values="1 0 0 0 0  0 1 0 0 0  0 0 1 0 0  0 0 0 18 -7" result="goo" />
            <feComposite in="SourceGraphic" in2="goo" operator="atop" />
          </filter>
        </svg>
      )}

      <div
        style={{
          filter: useFilter ? "url(#blob-filter)" : "none",
          width: "100%",
          height: "100%",
          position: "absolute",
          top: 0,
          left: 0,
        }}
      >
        {Array.from({ length: trailCount }).map((_, index) => (
          <div
            key={index}
            ref={(el) => (blobsRef.current[index] = el)}
            style={{
              position: "absolute",
              top: 0,
              left: 0,
              width: sizes[index] || 60,
              height: sizes[index] || 60,
              backgroundColor: fillColor,
              borderRadius: blobType === "circle" ? "50%" : "0",
              opacity: opacities[index] || 0.6,
              boxShadow: `${shadowOffsetX}px ${shadowOffsetY}px ${shadowBlur}px ${shadowColor}`,
              transform: "translate(-50%, -50%)",
              pointerEvents: "none",
            }}
          >
            {innerSizes[index] > 0 && (
              <div
                style={{
                  position: "absolute",
                  top: "50%",
                  left: "50%",
                  transform: "translate(-50%, -50%)",
                  width: innerSizes[index],
                  height: innerSizes[index],
                  backgroundColor: innerColor,
                  borderRadius: "50%",
                }}
              />
            )}
          </div>
        ))}
      </div>
    </div>
  )
}

export default BlobCursor
